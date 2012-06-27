meta-vexa15
===========

Yocto layer for Versatile Express A15 including big.LITTLE

The following is taken from Ken Werner's wiki page at Linaro - https://wiki.linaro.org/KenWerner/Sandbox/OpenEmbedded-Core. Please refernece that page as it may be updated more than this readme ;)

OE-core + meta-linaro
---------------------

<span class="anchor" id="line-4"></span>

-   This section describes how to setup and use an [OpenEmbedded-Core][]
    build environment including the Linaro layer. The [meta-linaro][]
    layer focuses on toolchain components and employs recipes for
    building the [Linaro GCC][] the OE-core way. The layer also extends
    the default OE-core kernel recipe (yocto) to build a kernel for the
    qemuarmv7a MACHINE using a vexpress defconfig in order to get an
    image that targets ARMv7 and can be booted using QEMU.
    <span class="anchor" id="line-5"></span><span class="anchor" id="line-6"></span>

### prerequisites

<span class="anchor" id="line-7"></span>

-   The build system I’m using is a x86\_64 machine running Ubuntu 12.04
    (Precise). I’ve installed the following packages:
    <span class="anchor" id="line-8"></span><span class="anchor" id="line-9"></span><span class="anchor" id="line-10"></span>

        sudo apt-get install sed wget cvs subversion git-core bzr coreutils unzip gawk python diffstat make build-essential gcc g++ desktop-file-utils chrpath autoconf automake libgl1-mesa-dev libglu1-mesa-dev libsdl1.2-dev text2html texinfo

    <span class="anchor" id="line-11"></span><span class="anchor" id="line-12"></span>

### fetch the sources

<span class="anchor" id="line-13"></span>

<ul>
<li style="list-style-type: none;">
<span class="anchor" id="line-14"></span><span class="anchor" id="line-15"></span><span class="anchor" id="line-16"></span><span class="anchor" id="line-17"></span><span class="anchor" id="line-18"></span><span class="anchor" id="line-19"></span><span class="anchor" id="line-20"></span><span class="anchor" id="line-21"></span><span class="anchor" id="line-22"></span><span class="anchor" id="line-23"></span><span class="anchor" id="line-24"></span><span class="anchor" id="line-25"></span><span class="anchor" id="line-26"></span>

    mkdir oe && cd oe
    git clone git://git.openembedded.org/openembedded-core
    git clone git://git.linaro.org/people/kwerner/meta-linaro.git
    cd meta-linaro
    git checkout -b 4.7-2012.06 4.7-2012.06
    cd ..
    cd openembedded-core
    git checkout -b 363424c 363424c
    git clone git://git.openembedded.org/bitbake bitbake
    cd bitbake
    git checkout -b f8bf449 f8bf449
    cd ..

<span class="anchor" id="line-27"></span><span class="anchor" id="line-28"></span>Most
of the patches to OE-core that are required for the meta-linaro are
upstream already. Currently there is only one optional patch that
removes the -no-tree-vectorize GCC option for armv7a machines.
<span class="anchor" id="line-29"></span><span class="anchor" id="line-30"></span><span class="anchor" id="line-31"></span>

    wget -q -O - http://people.linaro.org/~kwerner/oe-core/patches/armv7a-tree-vectorize.patch | patch -p1

### set up the environment

<span class="anchor" id="line-34"></span>

-   The next step is to create the build directory and setting up an
    approriate configuration.
    <span class="anchor" id="line-35"></span><span class="anchor" id="line-36"></span><span class="anchor" id="line-37"></span><span class="anchor" id="line-38"></span><span class="anchor" id="line-39"></span><span class="anchor" id="line-40"></span><span class="anchor" id="line-41"></span>

        # source the env script 
        #   this will create the build directory 
        #   including a default bblayers.conf and local.conf
        #   and finally change into build dir:
        . ./oe-init-build-env ../build

    <span class="anchor" id="line-42"></span><span class="anchor" id="line-43"></span>

#### add the Linaro and Versatile Express A15 layers

<span class="anchor" id="line-44"></span>

-   Add the Linaro meta layer by editing the conf/bblayers.conf. The
    BBLAYERS variable should look like this:
    <span class="anchor" id="line-45"></span><span class="anchor" id="line-46"></span><span class="anchor" id="line-47"></span><span class="anchor" id="line-48"></span><span class="anchor" id="line-49"></span>

        BBLAYERS = " \
          /path/to/meta-linaro \
          /path/to/meta-vexa15 \
          /path/to/oe/openembedded-core/meta"

    <span class="anchor" id="line-50"></span><span class="anchor" id="line-51"></span>

#### adjust your conf/local.conf

<span class="anchor" id="line-52"></span>

<ul>
<li style="list-style-type: none;">
<span class="anchor" id="line-53"></span><span class="anchor" id="line-54"></span><span class="anchor" id="line-55"></span><span class="anchor" id="line-56"></span><span class="anchor" id="line-57"></span><span class="anchor" id="line-58"></span><span class="anchor" id="line-59"></span><span class="anchor" id="line-60"></span><span class="anchor" id="line-61"></span><span class="anchor" id="line-62"></span><span class="anchor" id="line-63"></span><span class="anchor" id="line-64"></span><span class="anchor" id="line-65"></span>

    # set the default machine and target
    MACHINE = "qemuarmv7a"
    DEFAULTTUNE = "armv7athf-neon"
    ARM_INSTRUCTION_SET = "thumb"

    # specify the alignment of the root file system
    # this is required when building for qemuarmv7a
    IMAGE_ROOTFS_ALIGNMENT = "2048"

    # optionally alter the following variables depending on your build machine:
    BB_NUMBER_THREADS
    PARALLEL_MAKE

#### choosing a toolchain

<span class="anchor" id="line-68"></span>

-   By default the toolchain provided by OE-core gets used. In order to
    build OE-Core using the Linaro GCC you may add the following to your
    conf/local.conf:
    <span class="anchor" id="line-69"></span><span class="anchor" id="line-70"></span><span class="anchor" id="line-71"></span><span class="anchor" id="line-72"></span>

        GCCVERSION = "linaro-4.7"
        SDKGCCVERSION = "linaro-4.7"

    <span class="anchor" id="line-73"></span>The choices are:
    <span class="anchor" id="line-74"></span>

    -   ‘4.6%’ - GCC 4.6 based toolchain provided by OE-Core
        <span class="anchor" id="line-75"></span>
    -   ‘4.7%’ - GCC 4.7 based toolchain provided by OE-Core
        <span class="anchor" id="line-76"></span>
    -   ‘linaro-4.6’ - Linaro GCC 4.6 based toolchain provided by
        meta-linaro <span class="anchor" id="line-77"></span>
    -   ‘linaro-4.7’ - Linaro GCC 4.7 based toolchain provided by
        meta-linaro
        <span class="anchor" id="line-78"></span><span class="anchor" id="line-79"></span>

### build the images

<span class="anchor" id="line-80"></span>

<ul>
<li style="list-style-type: none;">
First, bitbake will build some native tools like m4, autoconf, libtool
and install them into a sysroot. The tools of the sysroot and the binary
toolchain are used to compile the sources for the target including a
Linaro 3.1 based Linux kernel configured for the versatile express
board. It all gets packaged up (ipk by default) and installed into a
root fs.
<span class="anchor" id="line-81"></span><span class="anchor" id="line-82"></span><span class="anchor" id="line-83"></span><span class="anchor" id="line-84"></span><span class="anchor" id="line-85"></span><span class="anchor" id="line-86"></span><span class="anchor" id="line-87"></span><span class="anchor" id="line-88"></span><span class="anchor" id="line-89"></span><span class="anchor" id="line-90"></span>

    # just enough to get a busybox prompt
    bitbake core-image-minimal

    # Xorg, gtk+2
    bitbake core-image-sato

    # Qt 4 embedded
    bitbake qt4e-demo-image

<span class="anchor" id="line-91"></span><span class="anchor" id="line-92"></span>The
images will land in:
<span class="anchor" id="line-93"></span><span class="anchor" id="line-94"></span><span class="anchor" id="line-95"></span><span class="anchor" id="line-96"></span><span class="anchor" id="line-97"></span><span class="anchor" id="line-98"></span><span class="anchor" id="line-99"></span><span class="anchor" id="line-100"></span>

    # the image output lands at:
    ls -l tmp-eglibc/deploy/images/
    # kernel:
    ls -l tmp-eglibc/deploy/images/zImage-qemuarmv7a.bin
    # fs image:
    ls -l tmp-eglibc/deploy/images/*-qemuarmv7a.ext3

### use QEMU to boot the images

<span class="anchor" id="line-103"></span>

-   As part of the OE-core build process qemu will be compiled. There is
    a wrapper script called runqemu that can be used to run the default
    OE-core images. Since we added a new qemuarmv7a MACHINE the runqemu
    script wouldn’t work for us. However, we can easily run qemu
    manually:
    <span class="anchor" id="line-104"></span><span class="anchor" id="line-105"></span>
-   minimal image
    <span class="anchor" id="line-106"></span><span class="anchor" id="line-107"></span><span class="anchor" id="line-108"></span><span class="anchor" id="line-109"></span><span class="anchor" id="line-110"></span><span class="anchor" id="line-111"></span><span class="anchor" id="line-112"></span><span class="anchor" id="line-113"></span>

        tmp-eglibc/sysroots/x86_64-linux/usr/bin/qemu-system-arm \
         -M vexpress-a9 -m 1024 -serial stdio -display none \
         -snapshot -no-reboot \
         -kernel tmp-eglibc/deploy/images/zImage-qemuarmv7a.bin \
         -drive file=tmp-eglibc/deploy/images/core-image-minimal-qemuarmv7a.ext3,if=sd,cache=writeback \
         --append "rw console=ttyAMA0,38400n8 console=tty root=/dev/mmcblk0"

    <span class="anchor" id="line-114"></span><span class="anchor" id="line-115"></span>

-   sato image
    <span class="anchor" id="line-116"></span><span class="anchor" id="line-117"></span><span class="anchor" id="line-118"></span><span class="anchor" id="line-119"></span><span class="anchor" id="line-120"></span><span class="anchor" id="line-121"></span><span class="anchor" id="line-122"></span><span class="anchor" id="line-123"></span>

        tmp-eglibc/sysroots/x86_64-linux/usr/bin/qemu-system-arm \
         -M vexpress-a9 -m 1024 -serial stdio \
         -snapshot -no-reboot -show-cursor \
         -kernel tmp-eglibc/deploy/images/zImage-qemuarmv7a.bin \
         -drive file=tmp-eglibc/deploy/images/core-image-sato-qemuarmv7a.ext3,if=sd,cache=writeback \
         --append "rw console=ttyAMA0,38400n8 console=tty root=/dev/mmcblk0"

    <span class="anchor" id="line-124"></span><span class="anchor" id="line-125"></span>

-   Qt4e image
    <span class="anchor" id="line-126"></span><span class="anchor" id="line-127"></span><span class="anchor" id="line-128"></span><span class="anchor" id="line-129"></span><span class="anchor" id="line-130"></span><span class="anchor" id="line-131"></span><span class="anchor" id="line-132"></span><span class="anchor" id="line-133"></span>

        tmp-eglibc/sysroots/x86_64-linux/usr/bin/qemu-system-arm \
         -M vexpress-a9 -m 1024 -serial stdio \
         -snapshot -no-reboot \
         -kernel tmp-eglibc/deploy/images/zImage-qemuarmv7a.bin \
         -drive file=tmp-eglibc/deploy/images/qt4e-demo-image-qemuarmv7a.ext3,if=sd,cache=writeback \
         --append "rw console=ttyAMA0,38400n8 console=tty root=/dev/mmcblk0"

    <span class="anchor" id="line-134"></span><span class="anchor" id="line-135"></span>

### kernel debugging

<span class="anchor" id="line-136"></span>

-   It’s quite convenient to debug the Linux kernel using QEMU. All you
    need is to install a GDB that is able to debug ARM code and to add
    “-s -S” to the qemu command line (-s: starts gdbserver stub on
    tcp::1234, S: freezes the CPU at startup).
    <span class="anchor" id="line-137"></span><span class="anchor" id="line-138"></span><span class="anchor" id="line-139"></span><span class="anchor" id="line-140"></span><span class="anchor" id="line-141"></span><span class="anchor" id="line-142"></span><span class="anchor" id="line-143"></span><span class="anchor" id="line-144"></span><span class="anchor" id="line-145"></span><span class="anchor" id="line-146"></span><span class="anchor" id="line-147"></span><span class="anchor" id="line-148"></span><span class="anchor" id="line-149"></span><span class="anchor" id="line-150"></span>

        # prereqs:
        sudo apt-get install gdb-multiarch

        # start qemu (see above) and add the "-s -S" options
        tmp-eglibc/sysroots/x86_64-linux/usr/bin/qemu-system-arm <other args> -s -S &

        # then start the gdb and attach it to QEMU
        gdb-multiarch tmp-eglibc/work/qemuarmv7a-oe-linux-gnueabi/linux-linaro-*/git/vmlinux
        (gdb) target remote localhost:1234
        (gdb) directory tmp-eglibc/work/qemuarmv7a-oe-linux-gnueabi
        (gdb) break do_execve
        (gdb) continue

    <span class="anchor" id="line-151"></span><span class="anchor" id="line-152"></span>

### cleanup

<span class="anchor" id="line-153"></span>

-   <span class="anchor" id="line-154"></span><span class="anchor" id="line-155"></span><span class="anchor" id="line-156"></span><span class="anchor" id="line-157"></span><span class="anchor" id="line-158"></span>

        bitbake -ccleansstate world

        # if you want to remove everything you may type:
        rm -rf sstate-cache tmp* pseudodone

    <span class="anchor" id="line-159"></span><span class="anchor" id="line-160"></span><span class="anchor" id="line-161"></span>

  [OpenEmbedded-Core]: http://www.openembedded.org
  [meta-linaro]: http://git.linaro.org/gitweb?p=people/kwerner/meta-linaro.git
  [Linaro GCC]: https://launchpad.net/gcc-linaro
