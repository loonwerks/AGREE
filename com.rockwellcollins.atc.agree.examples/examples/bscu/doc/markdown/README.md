# Brake System Control Unit (BSCU)

The Brake System Control Unit (BSCU) model illustrates the difficulty of
agreement between federated systems running asynchronously.  In this model a
redundantly controlled aircraft brake system is specified with 
quasi-synchronous execution and the agreement between the two systems as to
which is in control is examined under the possible presence of faults.  Users
are cautioned that the details of the interactions are subtle and due to the
asynchrony a large state space of possible execution orderings is required,
resulting in analysis runtimes in excess of 30 minutes.
