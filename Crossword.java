public final class Crossword implements WordPuzzleInterface {
    private StringBuilder[] colStr, rowStr;
    private DictInterface d;
    private char[][] b, solution;
    private boolean isSolved;

    public char[][] fillPuzzle(char[][] board, DictInterface dictionary) {
        this.b = board;
        this.d = dictionary;
        rowStr = new StringBuilder[b.length];
        colStr = new StringBuilder[b[0].length];
        solution = new char[board.length][board[0].length];

        for (int i = 0; i < b.length; i++) {
            colStr[i] = new StringBuilder();
            rowStr[i] = new StringBuilder();
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                rowStr[i].append(board[i][j]);
                colStr[j].append(board[i][j]);
            }
        }

        isSolved = false;

        fillPuzzleHelper(0, 0);

        if (isSolved)
            return solution;
        return null;
    }

    private void fillPuzzleHelper(int row, int col) {
        if (Character.isLetter(b[row][col])) {
            solution[row][col] = b[row][col];
            if (row == b.length - 1 && col == b[0].length - 1) {
                isSolved = true;
                return;
            }

            if (col == b[0].length - 1)
                fillPuzzleHelper(row + 1, 0);
            else
                fillPuzzleHelper(row, col + 1);
        } else if (b[row][col] == '+') {
            for (char ch = 'a'; ch <= 'z'; ch++) {
                rowStr[row].setCharAt(col, ch);
                colStr[col].setCharAt(row, ch);

                if (isValid(rowStr[row]) && isValid(colStr[col])) {
                    solution[row][col] = ch;
                    if (row == b.length - 1 && col == b[0].length - 1) {
                        isSolved = true;
                        return;
                    } else if (col == b[0].length - 1)
                        fillPuzzleHelper(row + 1, 0);
                    else
                        fillPuzzleHelper(row, col + 1);
                }
                if (isSolved)
                    break;
            }
            if (!isSolved) {
                rowStr[row].setCharAt(col, '+');
                colStr[col].setCharAt(row, '+');
                solution[row][col] = '+';
            }
        }
    }

    private boolean isValid(StringBuilder str) {
        StringBuilder subStr = new StringBuilder("");
        int result;
        for (int i = 0; i < b.length; i++) {
            char currentChar = str.charAt(i);
            if (Character.isDigit(currentChar)) {
                int limit = Character.getNumericValue(currentChar);
                int count = 0;
                for (int j = 0; j < str.length(); j++) {
                    if (str.charAt(j) == currentChar) {
                        count++;
                        if (count > limit) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < b.length; i++) {
            if (str.charAt(i) == '+') {
                if (subStr.length() == 0)
                    return true;
                result = d.searchPrefix(subStr);
                if (result != 0 && result != 2)
                    return true;
                return false;
            } else if (str.charAt(i) != '-') {
                subStr.append(str.charAt(i));
            } else if (subStr.length() > 1) {
                result = d.searchPrefix(subStr);
                if (result == 0 || result == 1)
                    return false;
                subStr = new StringBuilder("");

            } else {
                subStr = new StringBuilder("");
            }

        }
        return subStr.length() == 0 || d.searchPrefix(subStr) != 1 && d.searchPrefix(subStr) != 0;

    }

    public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary) {
        this.b = emptyBoard;
        this.d = dictionary;
        rowStr = new StringBuilder[b.length];
        colStr = new StringBuilder[b[0].length];

        for (int i = 0; i < b.length; i++) {
            colStr[i] = new StringBuilder();
            rowStr[i] = new StringBuilder();
        }

        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                if (filledBoard[i][j] == '+')
                    return false;
                if (emptyBoard[i][j] == '+' && filledBoard[i][j] == '-')
                    return false;
                if (emptyBoard[i][j] != '+' && emptyBoard[i][j] != filledBoard[i][j])
                    return false;
                colStr[j].append(filledBoard[i][j]);
                rowStr[i].append(filledBoard[i][j]);
            }
        }

        for (StringBuilder s : colStr)
            if (!isValid(s))
                return false;
        for (StringBuilder s : rowStr)
            if (!isValid(s))
                return false;

        return true;
    }

}