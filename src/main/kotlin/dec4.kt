
fun main(args: Array<String>) {

    val input = Utils.readFileAsListOfStrings("src/main/resources/dec4_input.txt")

    println("we have ${input.size} items")
    val game = processInput(input)

    dec4_part1(game)
    dec4_part2(game)
}

private fun dec4_part1(game: Game) {
    println("part1...")
    var complete = false
    var counter = 0
    val playedNumbers = arrayListOf<Int>()
    var completedBoardIndex = -1

    while (counter < game.numbers.size && !complete) {
        playedNumbers.add(game.numbers[counter])

        val isAnyBoardComplete = game.isAnyBoardComplete(playedNumbers)
        if (isAnyBoardComplete != -1){
            complete = true
            completedBoardIndex = isAnyBoardComplete
        }

        counter++

    }

    println("we have a complete game after ${playedNumbers.size}")
    println("board index $completedBoardIndex")


    game.sumBoard(completedBoardIndex, playedNumbers)

}

private fun dec4_part2(game: Game) {
    println("part2...")
    var complete = false
    var counter = 0
    val playedNumbers = arrayListOf<Int>()
    var previousCompleteBoards = arrayListOf<Int>()
    var newCompleteBoards = arrayListOf<Int>()

    while (counter < game.numbers.size && !complete) {
        playedNumbers.add(game.numbers[counter])

        newCompleteBoards = game.listOfCompleteBoards(playedNumbers)
        if (newCompleteBoards.size == game.boards.size){
            complete = true
        } else {
            previousCompleteBoards = newCompleteBoards
        }

        counter++
    }

    newCompleteBoards.removeAll(previousCompleteBoards)

    game.sumBoard(newCompleteBoards.first(), playedNumbers)

}

private data class Game(val numbers: List<Int>, val boards: List<Board>) {

    fun isBoardComplete(board: Board, calledNumbers: List<Int>): Boolean {
        var complete = false

        // check rows
        for (i in board.rows.indices) {
            val row = board.rows[i]
            if (calledNumbers.containsAll(row)) complete = true
        }

        // check columns
        val columns = Utils.transpose(board.rows)

        for (i in columns!!.indices) {
            if (calledNumbers.containsAll(columns[i]!!)) complete = true
        }


        return complete
    }

    fun isAnyBoardComplete(calledNumbers: List<Int>): Int {
        var complete = -1

        for (i in this.boards.indices) {
            if (isBoardComplete(this.boards[i], calledNumbers)) complete = i
        }

        return complete
    }

    fun listOfCompleteBoards(calledNumbers: List<Int>) : ArrayList<Int> {
        var list = arrayListOf<Int>()

        for (i in this.boards.indices) {
            if (isBoardComplete(this.boards[i], calledNumbers)) list.add(i)
        }

        return list
    }

    fun sumBoard(boardIndex: Int, calledNumbers: List<Int>) {
        val board = this.boards[boardIndex]
        val allNumbers = arrayListOf<Int>()

        for (i in board.rows.indices) {
            allNumbers.addAll(board.rows[i])
        }

        var sum = 0
        allNumbers
            .filterNot { calledNumbers.contains(it) }
            .map { sum+=it }

        val calculation = sum * calledNumbers.last()

        println("calculation = $calculation")
    }
}
private data class Board(var rows: ArrayList<List<Int>> = arrayListOf()) {
    fun addRow(row: List<Int>) {
        rows.add(row)
    }
}

private fun processInput(input: List<String>) : Game {
    // first row is the game numbers
    val numbers: List<Int> = input[0].split(",").map { it.toInt() }

    // then it's a 5x5 grid for each board
    val boards = arrayListOf<Board>()
    var currentBoard = Board()

    for (i in 2 until input.size) {
        val inputRow = input[i]
        if (inputRow.isBlank()) {
            //end of record -> add board to boards and clear
            boards.add(currentBoard)
            currentBoard = Board()
        } else {
            val row: List<Int> = inputRow.split(" ")
                .filterNot { it.isBlank() }
                .map { it.toInt() }
            currentBoard.addRow(row)
        }
    }

    boards.add(currentBoard)

    return Game(numbers, boards)
}

