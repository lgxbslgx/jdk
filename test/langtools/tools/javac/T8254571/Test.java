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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A test demo which is provided by bug reporter.
 * See JDK-8254571 for more information.
 * This file is compiled by T8254571.java
 */
public class Test {
    public static void main(String[] args) {
        process(new ArrayList<>());
    }

    public static String[] process(List<Resource> resources) {
        return resources.stream()
                .map(Lambda.function(resource -> Mapper.toString(resource),
                        (res, ex) -> Lambda.sneakyThrow(ex)))
                .toArray(String[]::new);
    }
}

interface Resource {
    InputStream getInputStream() throws IOException;
}

interface Function1<T, R> extends Function<T, R> {
    interface WithException<T, R, E extends Throwable> {
        R apply(T t) throws E;

        default Function1<T, R> cast(BiFunction<T, Throwable, R> fn) {
            return t -> {
                try {
                    return apply(t);
                } catch (Throwable e) {
                    return fn.apply(t, e);
                }
            };
        }
    }
}

final class Lambda {
    public static <T, R, E extends Throwable> Function<T, R> function(Function1.WithException<T, R, E> function,
                                                                      BiFunction<T, Throwable, R> errorFn) {
        return function.cast(errorFn);
    }

    @SuppressWarnings("unchecked")
    public static <R, E extends Throwable> R sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }
}

class Mapper {
    static String toString(Resource resource) throws IOException {
        return resource.getInputStream().toString();
    }
}