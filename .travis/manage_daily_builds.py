#!/usr/bin/env python3

'''
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
'''

import os
import re
import sys

from github3 import GitHub
from pprint import pformat

GITHUB_API = 'https://api.github.com/repos'
GITHUB_RELEASES = 'releases'
AUTH_TOKEN = os.environ['GH_TOKEN'] if 'GH_TOKEN' in os.environ.keys() else None

REPOSITORY_OWNER = 'loonwerks'
REPOSITORY_REPO = 'AGREE'

PRODUCT_ASSET_PATTERN = re.compile(r'com.rockwellcollins.atc.agree.repository-\d+\.\d+\.\d+(-(\d{12}))?-.*')

def manage_daily_builds(sname):
    print('Managing builds matching %s' % (sname))
    # obtain git handle
    gh = GitHub(GITHUB_API, token=AUTH_TOKEN)
    repository = gh.repository(REPOSITORY_OWNER, REPOSITORY_REPO)
    # get list of releases
    releases = repository.releases()
    # extract keys and sort by build date
    release_keys = {x.id : x.created_at for x in releases if sname in x.name} 
    sorted_keys = sorted(release_keys.items(), reverse=True, key=lambda x: x[1])
    print('%s' % (pformat(sorted_keys)))
    # filter to obtain the keys to delete
    delete_keys = [v[0] for v in sorted_keys[2:]]
    print('Deleting releases: %s' % (pformat(delete_keys)))
    # iterate, deleting the releases and corresponding tags
    for rel in releases:
        print('examining rel %d from %s...' % (rel.id, str(rel.created_at)))
        if rel.id in delete_keys and rel.tag_name is not None:
            print(' deleting release id %d and tag %s.' % (rel.id, rel.tag_name))
            rel_tag_ref = repository.ref('tags/%s' % (rel.tag_name))
            rel.delete()
            if rel_tag_ref is not None:
                print('  deleting tag %s' % (rel_tag_ref.ref))
                rel_tag_ref.delete()
        else:
            # Look for stale files in the release
            assets = rel.assets()
            print('In release %s found assets:' % (rel.name))
            for asset in assets:
                match = PRODUCT_ASSET_PATTERN.search(asset.name)
                print('  asset named %s matches %s' % (asset.name, match.group(1) if match is not None else 'None'))
            build_times = sorted([PRODUCT_ASSET_PATTERN.search(x.name).group(1) for x in assets if PRODUCT_ASSET_PATTERN.search(x.name)])
            latest_build_time = build_times[-1] if build_times else None
            print('Lastest build time is %s' % (latest_build_time))
            for asset in assets:
                match = PRODUCT_ASSET_PATTERN.search(asset.name)
                # print('  asset named %s matches %s' % (asset.name, match.group(1) if match is not None else 'None'))
                if match is not None:
                    asset_build_time = match.group(1)
                    if asset_build_time != latest_build_time:
                        print('deleting stale asset %s' % (asset.name))
                        asset.delete()

if __name__ == '__main__':
    manage_daily_builds(sys.argv[1])
