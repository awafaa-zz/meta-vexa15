require linux.inc

FILESPATHPKG =. "linux-vexa15-3.5.0rc4/${MACHINE}:"

SRCREV = "3.5.0rc4"
PV = "3.5.0rc4"

PR = "r0"

#SRC_URI = "git://git.linaro.org/people/amitk/linux-2.6.git;branch=wip-efikamx-cleanup3;protocol=git \
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux.git;protocol=git \
	   file://defconfig"

COMPATIBLE_MACHINE = "(vexa15)"

S = "${WORKDIR}/git"

