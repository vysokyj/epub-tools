# replace Czech leading quote with entity
s/„/\&bdquo;/g

# replace Czech ending quote with entity
s/“/\&ldquo;/g

# Smart quotes - value in quotes - UNSUSED NOW
#s/"([^"]*)"/\&bdquo;$1\&ldquo;/g

# Smart quotes - starting quote standalone
s/([\s:;])?"\b/$1&bdquo;/g

# Smart quotes - ending quote standalone
s/([\w\.,?!;])"([\s,;])?/$1&ldquo;$2/g

# add non breaking spaces
s/([\s;][szkvaiouSZKVAIOU]) /$1\&nbsp;/g

# replace minus with Czech dash entity
s/\s-\s/ \&ndash; /g