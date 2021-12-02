import java.io.File

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
    }



}