#!/usr/bin/env bash
set -e
BRANCH=samples
CURDIR=$(cd $(dirname "$0") && pwd)
SAMPLES_DIR=samples
GH_REMOTE="git@github.com:snaekobbi/braille-rendition-epub.git"
TMP_DIR=$( mktemp -t "$(basename "$0").XXXXXX" )
rm $TMP_DIR
cp -r $SAMPLES_DIR $TMP_DIR
cd $TMP_DIR
git init
git add . && git commit -m "publish samples"
git push --force $GH_REMOTE master:$BRANCH
cd $CURDIR
rm -rf $TMP_DIR
