#!/usr/bin/env python

"""
Pandoc filter to convert all links to cross-references.

Copyright (c) 2021, Collins Aerospace.
Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).

Permission is hereby granted, free of charge, to any person obtaining a copy of this data, 
including any software or models in source or binary form, as well as any drawings, specifications, 
and documentation (collectively &quot;the Data&quot;), to deal in the Data without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or 
substantial portions of the Data.

THE DATA IS PROVIDED &quot;AS IS&quot;, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
"""

from pandocfilters import attributes, toJSONFilter, Cite, Link, Space, Span, Str
import urlparse

def is_absolute(url):
    return bool(urlparse.urlparse(url).netloc)

def links(key, value, format, meta):
    if key == 'Link':
        [_, title, target] = value
        if (is_absolute(target[0])):
            # citation = [{"citationSuffix"  : [],
            #              "citationNoteNum" : 0,
            #              "citationMode"    : {"t":"NormalCitation"},
            #              "citationPrefix"  : [],
            #              "citationId"      : target[0],
            #              "citationHash"    : 0}]
            # return Cite(citation, title)
            return Span(attributes({}),
                        [Str(u'\u201c'), Span(attributes({}), title), Str(u'\u201d'),
                         Space(), Str('('), Str(target[0]), Str(')')])
        else:
            [_, _, targetInternal] = target[0].rpartition('#')
            citation = [{"citationSuffix"  : [],
                         "citationNoteNum" : 0,
                         "citationMode"    : {"t":"NormalCitation"},
                         "citationPrefix"  : [],
                         "citationId"      : targetInternal,
                         "citationHash"    : 0}]
            return Cite(citation, [Str("[@{0}]".format(targetInternal))])

if __name__ == "__main__":
    toJSONFilter(links)