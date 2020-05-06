package edu.umn.cs.crisys.agree.realtime;

import static jkind.lustre.LustreUtil.FALSE;
import static jkind.lustre.LustreUtil.and;
import static jkind.lustre.LustreUtil.arrow;
import static jkind.lustre.LustreUtil.eq;
import static jkind.lustre.LustreUtil.equal;
import static jkind.lustre.LustreUtil.id;
import static jkind.lustre.LustreUtil.integer;
import static jkind.lustre.LustreUtil.ite;
import static jkind.lustre.LustreUtil.less;
import static jkind.lustre.LustreUtil.minus;
import static jkind.lustre.LustreUtil.not;
import static jkind.lustre.LustreUtil.or;
import static jkind.lustre.LustreUtil.plus;
import static jkind.lustre.LustreUtil.pre;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rockwellcollins.atc.agree.analysis.AgreeLayout;
import com.rockwellcollins.atc.agree.analysis.AgreeRenaming;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNode;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeNodeBuilder;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeProgram;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeStatement;
import com.rockwellcollins.atc.agree.analysis.ast.AgreeVar;
import com.rockwellcollins.atc.agree.analysis.extentions.AgreeAutomater;
//import com.rockwellcollins.atc.agree.analysis.lustre.visitors.IdGatherer;

import jkind.api.results.AnalysisResult;
import jkind.lustre.BinaryExpr;
import jkind.lustre.BinaryOp;
import jkind.lustre.Equation;
import jkind.lustre.Expr;
import jkind.lustre.IdExpr;
import jkind.lustre.IntExpr;
import jkind.lustre.NamedType;
import jkind.lustre.Node;
import jkind.lustre.NodeCallExpr;
import jkind.lustre.TupleExpr;
import jkind.lustre.UnaryExpr;
import jkind.lustre.UnaryOp;
import jkind.lustre.VarDecl;
import jkind.lustre.builders.NodeBuilder;
import jkind.lustre.visitors.ExprMapVisitor;
import jkind.lustre.visitors.PrettyPrintVisitor;

public class ConstraintsGenerator implements AgreeAutomater {

	int clkCnt = 0;
	AgreeNode mTopNode;
	List<AgreeVar> mT_inputs = new ArrayList<>();
	List<AgreeStatement> mT_assertions = new ArrayList<>();

	@Override
	public AgreeProgram transform(AgreeProgram program) {

		// Reset any leftovers from previous runs
		clkCnt = 0;
		mT_inputs.clear();
		mT_assertions.clear();

		// Make local copies of the final structures that we need to modify
		mTopNode = program.topNode;
		mT_inputs.addAll(mTopNode.inputs);
		mT_assertions.addAll(mTopNode.assertions);

		List<Node> lustreNodes = new ArrayList<>();
		lustreNodes.addAll(program.globalLustreNodes);

		boolean test = true;

		if (test) {
			addClocks();
			lustreNodes = addCSLNodes(lustreNodes);

			// Only add the calendar nodes if at least one calendar type was created.
			if (clkCnt > 0) {
				lustreNodes = buildClkNode(lustreNodes);
				lustreNodes = buildMinNode(lustreNodes);

				// assert (next_inc = (0 -> (pre next2)));
				Expr clkIncrAssertion = equal(id("__NEXT_INC"),
						arrow(integer(0), pre(id("__NEXT".concat(String.valueOf(clkCnt - 1))))));
				mT_assertions.add(new AgreeStatement("", clkIncrAssertion, null));

				// check for 'time'
				for (VarDecl output : mTopNode.outputs) {
					if (new String("time").equals(output.id)) {
						IdExpr time = id("time");
						IdExpr nextInc = id("__NEXT_INC");
						Expr binexp = arrow(integer(0), plus(pre(time),nextInc));
						Expr addTime = equal(time, binexp);
						mT_assertions.add(new AgreeStatement("", addTime, null));
					}
				}
			}
		}

//		Property commTimingProp = EMFIndexRetrieval.getPropertyDefinitionInWorkspace(OsateResourceUtil.getResourceSet(),
//				"Timing_Properties::Period");
//
//		long intVal = PropertyUtils.getIntegerValue(program.topNode.compInst, commTimingProp);
//		PropertyUtils.getEnumLiteral(conn, commTimingProp);

		AgreeNodeBuilder agreeNode = new AgreeNodeBuilder(program.topNode.id);
		agreeNode.addInput(mT_inputs);
		agreeNode.addOutput(program.topNode.outputs);
		agreeNode.addLocal(program.topNode.locals);
		agreeNode.addConnection(program.topNode.connections);
		agreeNode.addSubNode(program.topNode.subNodes);
		agreeNode.addAssertion(mT_assertions);
		agreeNode.addAssumption(program.topNode.assumptions);
		agreeNode.addGuarantee(program.topNode.guarantees);
		agreeNode.addLemma(program.topNode.lemmas);
		agreeNode.setClockConstraint(program.topNode.clockConstraint);
		agreeNode.setInitialConstraint(program.topNode.initialConstraint);
		agreeNode.setClockVar(program.topNode.clockVar);
		agreeNode.setReference(program.topNode.reference);
		agreeNode.setTiming(program.topNode.timing);
		agreeNode.setCompInst(program.topNode.compInst);

		mTopNode = agreeNode.build();
		AgreeProgram newProgram = new AgreeProgram(program.agreeNodes, lustreNodes, program.globalTypes, mTopNode);
		return newProgram;
	}

//	Build the calendar nodes
	private List<Node> buildClkNode(List<Node> lustreNodes) {
		NodeBuilder node = new NodeBuilder("__CLOCK");

		IdExpr period = node.createInput("__PERIOD", NamedType.INT);
		IdExpr phase = node.createInput("__PHASE", NamedType.INT);
		IdExpr drift = node.createInput("__DRIFT", NamedType.INT);
		IdExpr tte = node.createInput("__TTE", NamedType.INT);
		IdExpr next_inc = node.createInput("__NEXT_INC", NamedType.INT);
		IdExpr tmr = node.createInput("__TMR", NamedType.INT);
		IdExpr prerun = node.createInput("__PRERUN", NamedType.BOOL);
		IdExpr m_next = node.createInput("__M_NEXT", NamedType.INT);

		IdExpr ntte = node.createOutput("__NTTE", NamedType.INT);
		IdExpr fired = node.createOutput("__FIRED", NamedType.BOOL);
		IdExpr run = node.createOutput("__RUN", NamedType.BOOL);
		IdExpr rnext = node.createOutput("__RNXT", NamedType.INT);

		// ntte = (if fired then tte else (if (PreRun and (not Run)) then ((period - tte) + drift) else (tmr - next_inc)));
		node.addEquation(eq(ntte,
				ite(fired, tte, ite(and(prerun, not(run)), plus(minus(period, tte), drift), minus(tmr, next_inc)))));

		// fired = (Run and (not PreRun));
		node.addEquation(eq(fired, and(run, not(prerun))));

		// Run = (((not PreRun) and ((tmr - next_inc) = 0)) or (PreRun and (not ((tmr - next_inc) = 0))));
		Expr T5 = and(not(prerun), equal(minus(tmr, next_inc), integer(0)));
		Expr T6 = and(prerun, not(equal(minus(tmr, next_inc), integer(0))));
		node.addEquation(eq(run, or(T5, T6)));

		// r_nxt = (if (m_nxt = (-1)) then ntte else Time_Funcs__min2(m_nxt, ntte));
		node.addEquation(eq(rnext, ite(equal(m_next, integer(-1)), ntte, new NodeCallExpr("__MIN2", m_next, ntte))));

		lustreNodes.add(node.build());
		return lustreNodes;
	}

	// Build the min node
	private List<Node> buildMinNode(List<Node> lustreNodes) {
		NodeBuilder node = new NodeBuilder("__MIN2");
		IdExpr A = node.createInput("__A", NamedType.INT);
		IdExpr B = node.createInput("__B", NamedType.INT);

		IdExpr MIN = node.createOutput("__MIN", NamedType.INT);

		node.addEquation(eq(MIN, ite(less(A, B), A, B)));

		lustreNodes.add(node.build());
		return lustreNodes;
	}

	private TupleExpr tuple(List<Expr> list) {
		return new TupleExpr(list);
	}

	// assert ((tmr1, fired1, run1, next1) =
	// Time_Funcs__periodic_proc(sys1.Period, sys1.Phase, sys1.Drift, sys1.tte, next_inc, (0 -> (pre tmr1)), (false -> (pre run1)), (-1)));
	private void addClocks() {

		for (VarDecl lustreVar : mTopNode.outputs) {
			AgreeVar var = (AgreeVar) lustreVar;
			if (var.type.toString().endsWith("__Clk_Stat")) {

				// The inputs
				IdExpr a = new IdExpr(var.id.concat(".Period"));
				IdExpr b = new IdExpr(var.id.concat(".Phase"));
				IdExpr c = new IdExpr(var.id.concat(".Drift"));
				IdExpr d = new IdExpr(var.id.concat(".tte"));
				IdExpr e = new IdExpr("__NEXT_INC");
				IdExpr f = new IdExpr("__TMR".concat(String.valueOf(clkCnt)));
				IdExpr g = new IdExpr(var.id.concat(".run"));

				// The outputs
				List<Expr> ids = new ArrayList<>();
				ids.add(new IdExpr("__TMR".concat(String.valueOf(clkCnt))));
				ids.add(new IdExpr(var.id.concat(".fired")));
				ids.add(new IdExpr(var.id.concat(".run")));
				ids.add(new IdExpr("__NEXT".concat(String.valueOf(clkCnt))));

				Expr newAssertion;
				if (clkCnt == 0) {
					newAssertion = equal(tuple(ids), new NodeCallExpr("__CLOCK", a, b, c, d, e, arrow(b, pre(f)),
							arrow(FALSE, pre(g)), integer(-1)));
					mT_inputs.add(new AgreeVar("__NEXT_INC", NamedType.INT, null, null));
				} else {
					Expr nodeCall = new NodeCallExpr("__CLOCK", a, b, c, d, e,
										equal(tuple(ids), arrow(b, pre(f)), arrow(FALSE, pre(g)), id("__NEXT".concat(String.valueOf(clkCnt - 1)))));
					;
					newAssertion = equal(tuple(ids), nodeCall);
				}

				mT_assertions.add(new AgreeStatement("", newAssertion, null));

				mT_inputs.add(new AgreeVar("__TMR".concat(String.valueOf(clkCnt)), NamedType.INT, null, null));
				mT_inputs.add(new AgreeVar("__RUN".concat(String.valueOf(clkCnt)), NamedType.BOOL, null, null));
				mT_inputs.add(new AgreeVar("__NEXT".concat(String.valueOf(clkCnt)), NamedType.INT, null, null));

				clkCnt++;
			}
		}
		return;
	}

	private List<Node> addCSLNodes(List<Node> lustreNodes) {

		List<AgreeStatement> origAssertions = new ArrayList<>();
		origAssertions.addAll(mT_assertions);

		List<Node> nodes = new ArrayList<>();
		nodes.addAll(lustreNodes);

		for (AgreeStatement nAssertion : origAssertions) {
			String tst = nAssertion.expr.toString();
			if (nAssertion.expr.toString().contains(new String("CSL__"))) {
				mT_assertions.add(updateCSLNodeCall(nAssertion));
				mT_assertions.remove(nAssertion);

			}
		}

		for (Node node : nodes) {
			if (node.id.startsWith("CSL__")) {
				lustreNodes.add(updateCSLNode(node));
				lustreNodes.remove(node);
			}
		}

		return lustreNodes;
	}

	private AgreeStatement updateCSLNodeCall(AgreeStatement assertion) {
		List<Expr> mInputs = new ArrayList<>();
		List<Expr> mOutputs = new ArrayList<>();

		PrettyPrintVisitor visitor2 = new PrettyPrintVisitor();
		assertion.expr.accept(visitor2);

		ExprMapVisitor visitor = new ExprMapVisitor();
		Expr tmp = assertion.expr.accept(visitor);
		Expr tmp2 = ((BinaryExpr) tmp).right;
		Iterator<Expr> iterator = ((NodeCallExpr) tmp2).args.iterator();
		String nodeName = (((NodeCallExpr) tmp2).node.toString()).substring(3);

		while (iterator.hasNext()) {
			mInputs.add(iterator.next());
		}
		mInputs.add(new BinaryExpr(new IntExpr(BigInteger.valueOf(-1)), BinaryOp.ARROW,
				new UnaryExpr(UnaryOp.PRE, new IdExpr("__T_REMAIN".concat(String.valueOf(clkCnt))))));
		mInputs.add(new IdExpr("__NEXT_INC"));
		if (clkCnt > 0) {
			mInputs.add(new IdExpr("__NEXT".concat(String.valueOf(clkCnt - 1))));
		} else {
			mInputs.add(new IntExpr(BigInteger.valueOf(-1)));
		}

		tmp2 = ((BinaryExpr) tmp).left;
		mOutputs.addAll(((TupleExpr) tmp2).elements);
		mOutputs.add(new IdExpr("__T_REMAIN".concat(String.valueOf(clkCnt))));
		mOutputs.add(new IdExpr("__NEXT".concat(String.valueOf(clkCnt))));
		mT_inputs.add(new AgreeVar("__T_REMAIN".concat(String.valueOf(clkCnt)), NamedType.INT, null, null));
		mT_inputs.add(new AgreeVar("__NEXT".concat(String.valueOf(clkCnt)), NamedType.INT, null, null));
		clkCnt++;

		Expr tmpExpr = new BinaryExpr(new TupleExpr(mOutputs), BinaryOp.EQUAL, new NodeCallExpr(nodeName, mInputs));

		AgreeStatement newAssertion = new AgreeStatement("", tmpExpr, null);

		return newAssertion;
	}

	private Node updateCSLNode(Node node) {
		NodeBuilder newNode = new NodeBuilder(node.id.toString().substring(3));

		newNode.addInputs(node.inputs);
		IdExpr T_REMAINING = newNode.createInput("T_REMAINING", NamedType.INT);
		IdExpr T_EXPIRED = newNode.createInput("T_EXPIRED", NamedType.INT);
		IdExpr CUR_MIN_T_REMAINING = newNode.createInput("CUR_MIN_T_REMAINING", NamedType.INT);

		newNode.addOutputs(node.outputs);
		IdExpr NEXT_T_REMAINING = newNode.createOutput("NEXT_T_REMAINING", NamedType.INT);
		IdExpr MIN_T_REMAINING = newNode.createOutput("MIN_T_REMAINING", NamedType.INT);

		// Flip through all the equations for this node and remove any that set our hidden variables to a value.
		for (Equation eq : node.equations) {
			for (IdExpr lhs : eq.lhs) {
				if (!lhs.id.contentEquals(new String("T_REMAINING")) && !lhs.id.contentEquals(new String("T_EXPIRED"))
						&& !lhs.id.contentEquals(new String("MIN_T_REMAINING"))) {
					newNode.addEquation(eq);
				}
			}
		}

		// -- Add a running check for the next min. A -1 resets the calculation
		// r_nxt = if ntr = -1 then m_nxt
		// else if m_nxt = -1 then ntr
		// else if m_nxt < ntr then m_nxt
		// else ntr;
		Expr T7 = equal(NEXT_T_REMAINING, integer(-1));
		Expr T8 = equal(CUR_MIN_T_REMAINING, integer(-1));
		Expr T9 = new NodeCallExpr("__MIN2", NEXT_T_REMAINING, CUR_MIN_T_REMAINING);
		newNode.addEquation(eq(MIN_T_REMAINING, ite(T7, CUR_MIN_T_REMAINING, ite(T8, NEXT_T_REMAINING, T9))));

		return newNode.build();
	}

	@Override
	public AgreeRenaming rename(AgreeRenaming renaming) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnalysisResult transformResult(AnalysisResult res) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgreeLayout transformLayout(AgreeLayout layout) {
		// TODO Auto-generated method stub
		return null;
	}
}
