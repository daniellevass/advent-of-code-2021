import java.io.File
import java.util.function.IntFunction
import java.util.function.ToIntFunction
import java.util.stream.Collectors

import java.util.stream.IntStream




class Utils {

    companion object {

        fun readFileAsListOfStrings(filename: String): List<String> {
            val list = arrayListOf<String>()
            File(filename)
                .forEachLine {
                    list.add(it)
                }

            return list
        }

        fun readFileAsListOfInts(filename: String): List<Int> {
            val list = arrayListOf<Int>()
            File(filename)
                .forEachLine {
                    list.add(it.toInt())
                }

            return list
        }

        fun <T> transpose(list: List<List<T>>): List<List<T>?>? {
            val N = list.stream().mapToInt { l: List<T> -> l.size }.max().orElse(-1)
            val iterList = list.stream().map { it: List<T> -> it.iterator() }.collect(Collectors.toList())
            return IntStream.range(0, N)
                .mapToObj { n: Int ->
                    iterList.stream()
                        .filter { it: Iterator<T> -> it.hasNext() }
                        .map { m: Iterator<T> -> m.next() }
                        .collect(Collectors.toList())
                }
                .collect(Collectors.toList())
        }
    }
}