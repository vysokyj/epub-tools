# replace Czech leading quote with entity
s/„/\&bdquo;/g

# replace Czech ending quote with entity
s/“/\&ldquo;/g

# Smart quotes - value in quotes - UNSUSED NOW
#s/"([^"]*)"/„$1“/g

# Smart quotes - starting quote standalone
s/([\s:;])?"\b/$1„/g

# Smart quotes - ending quote standalone
s/([\w\.,?!;])"([\s,;])?/$1“$2/g

# add non breaking spaces - note used non breaking space - not visible :-)
s/([\s;][szkvaiouSZKVAIOU]) /$1 /g

# replace minus with Czech dash entity
s/(\s)-(\s)/$1\–$2/g