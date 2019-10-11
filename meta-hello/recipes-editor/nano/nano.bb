SUMMARY = "Recipe to build the 'nano' editor"

PN = "nano"
PV = "2.2.6"

SITE = "http://www.nano-editor.org/dist"
PV_MAJOR = "${@d.getVar('PV',d,1).split('.')[0]}"
PV_MINOR = "${@d.getVar('PV',d,1).split('.')[1]}"

SRC_URI = "${SITE}/v${PV_MAJOR}.${PV_MINOR}/${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "03233ae480689a008eb98feb1b599807"
SRC_URI[sha256sum] = \
"be68e133b5e81df41873d32c517b3e5950770c00fc5f4dd23810cd635abce67a"

python do_fetch() {
    bb.plain("Downloading source tarball from ${SRC_URI} ...")
    src_uri = (d.getVar('SRC_URI', True) or "").split()
    if len(src_uri) == 0:
       bb.fatal("Empty URI")
    try:
       fetcher = bb.fetch2.Fetch(src_uri, d)
       fetcher.download()
    except bb.fetch2.BBFetchException:
       bb.fatal("Could not fetch source tarball.")
    bb.plain("Download successful.")
}

addtask fetch before do_build

do_unpack() {
    bbnote "Unpacking source tarball ..."
    tar x -C ${WORKDIR} -f ${DL_DIR}/${P}.tar.gz
    bbnote "Unpacked source tarball."
}

addtask unpack before do_build after do_fetch

do_configure() {
    bbnote "Configuring source package ..."
    cd ${WORKDIR}/${P} && ./configure
    bbnote "Configured source package."
}

addtask configure before do_build after do_unpack

do_compile() {
    bbnote "Compiling package..."
    cd ${WORKDIR}/${P} && make
    bbnote "Compiled package."
}

addtask compile before do_build after do_configure

do_clean[nostamp] = "1"
do_clean() {
    rm -rf ${WORKDIR}/${P}
    rm -f ${TMPDIR}/stamps/*
}

addtask clean
