require linux.inc

FILESPATHPKG =. "linux-vexa15-AEL3.3/${MACHINE}:"

SRCREV = "ael-12.03.00"
PV = "3.3.0"

PR = "r0"

#SRC_URI = "git://git.linaro.org/people/amitk/linux-2.6.git;branch=wip-efikamx-cleanup3;protocol=git \
SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/maz/arm-platforms.git;branch=ael-12.03.00;protocol=git \
	   file://defconfig"

COMPATIBLE_MACHINE = "(vexa15)"

S = "${WORKDIR}/git"

