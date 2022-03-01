#!/usr/bin/env python3

import urllib3
import certifi
import json

http = urllib3.PoolManager(10, headers={'user-agent': 'Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0'}, cert_reqs='CERT_REQUIRED', ca_certs=certifi.where())
r = http.request('GET', 'https://api.github.com/repos/loonwerks/agree/milestones?state=open&per_page=80')

data = json.loads(r.data.decode('utf-8'))

for elem in data:
    print('   ' + str(elem['number']) + '  ' + str(elem['title']))

