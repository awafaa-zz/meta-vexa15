Introduction
============

This describes how to setup and use an [OpenEmbedded](http://www.openembedded.org/) build environment with [Yocto](http://yoctoproject.org) including the Linaro layer and a layer that includes a Device Tree kernel specific to Versatile Express boards and models. 

The [meta-linaro](http://git.linaro.org/gitweb?p=openembedded/meta-linaro.git)
layer focuses on toolchain components and employs recipes for building
the [Linaro GCC](https://launchpad.net/gcc-linaro) the OE way.

For builds with "master" branch of OpenEmbedded Core some more work is
needed due to gcc-4.6 being removed from it (described later).

Please refer to the [Yocto Quick Start](http://www.yoctoproject.org/docs/current/yocto-project-qs/yocto-project-qs.html) and the [Linaro on OpenEmbedded wiki page](https://wiki.linaro.org/KenWerner/Sandbox/OpenEmbedded-Core) for any issues.

1. Prepare build environment
=========================

1.1 Prerequisites
----------------

Packages and package installation vary depending on your development
system. In general, you need to have root access and then install the
required packages. The next few sections show you how to get set up with
the right packages for Ubuntu, Fedora, openSUSE, and CentOS.

#### Ubuntu

The packages you need for a supported Ubuntu distribution are shown in
the following command:

~~~~ {.western}
     $ sudo apt-get install sed wget subversion git-core coreutils \
     unzip texi2html texinfo libsdl1.2-dev docbook-utils fop gawk \
     python-pysqlite2 diffstat make gcc build-essential xsltproc \
     g++ desktop-file-utils chrpath libgl1-mesa-dev libglu1-mesa-dev \
     autoconf automake groff libtool xterm libxml-parser-perl
                
~~~~

#### Fedora

The packages you need for a supported Fedora distribution are shown in
the following commands:

~~~~ {.western}
     $ sudo yum groupinstall "development tools"
     $ sudo yum install python m4 make wget curl ftp tar bzip2 gzip \
     unzip perl texinfo texi2html diffstat openjade \
     docbook-style-dsssl sed docbook-style-xsl docbook-dtds fop xsltproc \
     docbook-utils sed bc eglibc-devel ccache pcre pcre-devel quilt \
     groff linuxdoc-tools patch cmake \
     perl-ExtUtils-MakeMaker tcl-devel gettext chrpath ncurses apr \
     SDL-devel mesa-libGL-devel mesa-libGLU-devel gnome-doc-utils \
     autoconf automake libtool xterm
                
~~~~

#### openSUSE

The packages you need for a supported openSUSE distribution are shown in
the following command:

~~~~ {.western}
     $ sudo zypper install python gcc gcc-c++ libtool fop \
     subversion git chrpath automake make wget xsltproc \
     diffstat texinfo freeglut-devel libSDL-devel
                
~~~~

#### CentOS

The packages you need for a supported CentOS distribution are shown in
the following commands:

~~~~ {.western}
     $ sudo yum -y groupinstall "development tools"
     $ sudo yum -y install tetex gawk sqlite-devel vim-common redhat-lsb xz \
       m4 make wget curl ftp tar bzip2 gzip python-devel \
       unzip perl texinfo texi2html diffstat openjade zlib-devel \
       docbook-style-dsssl sed docbook-style-xsl docbook-dtds \
       docbook-utils bc glibc-devel pcre pcre-devel \
       groff linuxdoc-tools patch cmake \
       tcl-devel gettext ncurses apr \
       SDL-devel mesa-libGL-devel mesa-libGLU-devel gnome-doc-utils \
       autoconf automake libtool xterm
                
~~~~


1.2 Fetch the sources
--------------------

There are two branches that can be used: Master, which is the latest
code; Denzil, which is the latest official release from the Yocto
project. Decide which branch you wish to use and follow the steps
according to you branch choice.

### Common part

~~~~ {.western}
mkdir oe && cd oe
git clone git://git.linaro.org/openembedded/meta-linaro.git
git clone git://git.openembedded.org/openembedded-core
git clone git://github.com/awafaa/meta-vexa15.git
cd openembedded-core
git clone git://git.openembedded.org/bitbake bitbake
~~~~

### Master branch builds

~~~~ {.western style="margin-bottom: 0.5cm"}
cd .. && git clone git://git.openembedded.org/meta-openembedded
rm meta-linaro/recipes-lamp/mysql/*
~~~~

### Denzil branch builds

~~~~ {.western}
cd meta-linaro
git checkout -b 4.7-2012.06 4.7-2012.06
cd ..
cd openembedded-core
git checkout -b 363424c 363424c
cd bitbake
git checkout -b f8bf449 f8bf449
cd ..
~~~~

### Extra patches

Most of the patches to OE Core that are required for the meta-linaro are
upstream already. Currently there is only one **optional** patch that
removes the -no-tree-vectorize GCC option for armv7a machines.

~~~~ {.western}
cd openembedded-core
wget -q -O - http://people.linaro.org/~kwerner/oe-core/patches/armv7a-tree-vectorize.patch | patch -p1
~~~~

2. Setup the environment
========================

The next step is to create the build directory and setting up an
appropriate configuration.

~~~~ {.western style="margin-bottom: 0.5cm"}
. ./oe-init-build-env ../build
~~~~

2.1 Add the additional layers
-----------------------------

Add the Linaro and kernel meta layer by editing the conf/bblayers.conf.
The BBLAYERS variable should look like this:

### Master branch builds

~~~~ {.western}
BBLAYERS = " \
  ${TOPDIR}/../meta-vexa15 \
  ${TOPDIR}/../meta-linaro \
  ${TOPDIR}/../meta-openembedded/toolchain-layer \
  ${TOPDIR}/../openembedded-core/meta"
~~~~

### Denzil branch builds

~~~~ {.western}
BBLAYERS = " \
  ${TOPDIR}/../meta-vexa15 \
  ${TOPDIR}/../meta-linaro \
  ${TOPDIR}/../openembedded-core/meta"
~~~~

2.2 Adjust your conf/local.conf
-------------------------------

~~~~ {.western}
# set the default machine and target
MACHINE = "qemuarmv7a"
DEFAULTTUNE_qemuarmv7a = "armv7athf-neon"

# specify the alignment of the root file system
# this is required when building for qemuarmv7a
IMAGE_ROOTFS_ALIGNMENT = "2048"
~~~~

Also alter the following variables depending on your build machine:

~~~~ {.western}
BB_NUMBER_THREADS
PARALLEL_MAKE
~~~~

What values should one use? BB\_NUMBER\_THREADS controls how many tasks
are run at same time and "4" is a reasonable amount. Generally speaking
change the values to be twice the number of CPUs that your machine has.

#### Changing toolchain

By default the toolchain provided by OE Core gets used. In order to
build using the Linaro GCC the following lines needs to be added:

~~~~ {.western}
# Use Linaro's GCC
GCCVERSION = "linaro-4.7"
SDKGCCVERSION = "linaro-4.7"
~~~~

The choices are:

-   '4.6%' - GCC 4.6 based toolchain provided by OE Core (denzil) or
    OpenEmbedded (master)

-   '4.7%' - GCC 4.7 based toolchain provided by OE Core

-   'linaro-4.6' - Linaro GCC 4.6 based toolchain provided by
    meta-linaro

-   'linaro-4.7' - Linaro GCC 4.7 based toolchain provided by
    meta-linaro

#### Changing kernel

The meta-vexa15 layer provides two kernels, an upstream mainline kernel
and an AEL 3.3 kernel. To specify which one is used the following line
needs to be added:

~~~~ {.western}
PREFERRED_PROVIDER_virtual/kernel = "linux-ael"
~~~~

\
\

The choices are:

-   'linux-ael' – AEL 3.3 kernel

-   'linux-upstream' – Upstream mainline kernel

3. Build the images
===================

First, bitbake will build some native tools like m4, autoconf, libtool
and install them into a sysroot. The tools of the sysroot and the binary
toolchain are used to compile the sources for the target including a
Linaro 3.x based Linux kernel configured for the versatile express
board. It all gets packaged up (ipk by default) and installed into a
root fs.

~~~~ {.western}
# just enough to get a busybox prompt
bitbake core-image-minimal

# Xorg, gtk+2
bitbake core-image-sato

# Qt 4 embedded
bitbake qt4e-demo-image
~~~~

The images will be created in:

~~~~ {.western}
# the image output lands at:
ls -l tmp-eglibc/deploy/images/
# kernel:
ls -l tmp-eglibc/deploy/images/zImage-qemuarmv7a.bin
# fs image:
ls -l tmp-eglibc/deploy/images/*-qemuarmv7a.ext3
~~~~

4. Using images
===============

5.1 Use QEMU to boot the images
-------------------------------

As part of the OE Core build process qemu will be compiled. There is a
wrapper script called runqemu that can be used to run the default OE
Core images. Since we added a new qemuarmv7a MACHINE the runqemu script
wouldn't work for us. However, we can easily run qemu manually:

-   minimal image

~~~~ {.western}
tmp-eglibc/sysroots/x86_64-linux/usr/bin/qemu-system-arm \
 -M vexpress-a9 -m 1024 -serial stdio -display none \
 -snapshot -no-reboot \
 -kernel tmp-eglibc/deploy/images/zImage-qemuarmv7a.bin \
 -drive file=tmp-eglibc/deploy/images/core-image-minimal-qemuarmv7a.ext3,if=sd,cache=writeback \
 --append "rw console=ttyAMA0,38400n8 console=tty root=/dev/mmcblk0"
~~~~

-   sato image

~~~~ {.western}
tmp-eglibc/sysroots/x86_64-linux/usr/bin/qemu-system-arm \
 -M vexpress-a9 -m 1024 -serial stdio \
 -snapshot -no-reboot -show-cursor \
 -kernel tmp-eglibc/deploy/images/zImage-qemuarmv7a.bin \
 -drive file=tmp-eglibc/deploy/images/core-image-sato-qemuarmv7a.ext3,if=sd,cache=writeback \
 --append "rw console=ttyAMA0,38400n8 console=tty root=/dev/mmcblk0"
~~~~

-   Qt4e image

~~~~ {.western}
tmp-eglibc/sysroots/x86_64-linux/usr/bin/qemu-system-arm \
 -M vexpress-a9 -m 1024 -serial stdio \
 -snapshot -no-reboot \
 -kernel tmp-eglibc/deploy/images/zImage-qemuarmv7a.bin \
 -drive file=tmp-eglibc/deploy/images/qt4e-demo-image-qemuarmv7a.ext3,if=sd,cache=writeback \
 --append "rw console=ttyAMA0,38400n8 console=tty root=/dev/mmcblk0"
~~~~

4.2 Use FAST Model to boot the images
-------------------------------
W.I.P.

4.3 Use Versatile Express to boot the images
-------------------------------
W.I.P.

5. Clean up
===============

~~~~ {.western}
bitbake -ccleansstate world
~~~~

If you want to remove everything you can use
~~~~ {.western}
rm -rf sstate-cache tmp* pseudodone
~~~~ 
\
\

