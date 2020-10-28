/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8254571
 * @summary A specific exception(eg: IOException) should be in the range of type variable E which extends Throwable.
 * @compile T8254571.java Test.java
 */

import java.io.IOException;

/**
 * The minimized test according to the test demo(Test.java) in JDK-8254571
 */
public class T8254571 {
    public static void main(String[] args) {
        test(fun2(arg -> fun1()));
    }

    private static void fun1() throws IOException { }

    // The redundant R is necessary. Without R, the bug can't occur.
    private static <T, R, E extends Throwable> T fun2(WithException<T, E> function) {
        return null;
    }

    private static <T> void test(T t) { }
}

interface WithException<T, E extends Throwable> {
    void apply(T t) throws E;
}