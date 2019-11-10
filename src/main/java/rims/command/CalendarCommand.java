package rims.command;
import rims.core.ResourceList;
import rims.core.Ui;
import rims.core.Storage;
import rims.exception.RimsException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import static rims.command.ListCommand.getListForSpecificDay;
import static rims.command.ListCommand.stringToDate;

//@@author danielcyc
public class CalendarCommand extends Command {
    private static int cellLength = 15;
    private static int cellHeight = 6;
    private static int CalHeight = 5;
    private static int CalWidth = 7;

    private static int DaysInMonth = getDaysInMonth(LocalDateTime.now());

    private static String Vert = "|";
    private static String Horz = "-";
    private static String TopLeft = "+";
    private static String TopRight = "+";
    private static String BotRight = "+";
    private static String BotLeft = "+";
    private static String Centre = "+";
    private static String TopCentre = "+";
    private static String BotCentre = "+";
    private static String MidRight = "+";
    private static String MidLeft = "+";

    private static String[][] data;

    protected static ResourceList resources;
    protected static Ui ui;
    protected String operator;

    public CalendarCommand(ResourceList resources, Ui ui) {
        this.resources = resources;
        this.ui = ui;
        operator = null;
    }

    public CalendarCommand(ResourceList resources, Ui ui, String operator) {
        this.resources = resources;
        this.ui = ui;
        this.operator = operator;
    }

    // todo: check and align days of the week / offset from first box (mon)
    // todo: highlight box of today's date
    // todo: make calendar size dynamic - dependant on size of window
    // todo: make dates overflow from previous months
    // todo: make entry span across multiple cells
    // todo: change month function
    //
    // todo: implement calendar in correct format
    // todo: java docs

    // todo: add a resize function
    // todo: make headings - month and year as well as days of the week as the axis
    // todo: add "X more..." for when cell cannot display all rows

    public static void printCal() throws ParseException, RimsException {
        getData();
        printTopCells();
        for (int row = 2; row < CalHeight; row++) {
            printMidCells(row);
        }
        printBotCells();
    }

    private static void printTopCells() {
        int cellRow = 1;
        for (int i = 0; i <= (cellLength * CalWidth); i++) {
            if (i == 0) {
                System.out.print(TopLeft);
            } else if (i == (cellLength * CalWidth)) {
                System.out.print(TopRight);
            } else if ((i % cellLength) == 0) {
                System.out.print(TopCentre);
            } else {
                System.out.print(Horz);
            }
        }
        System.out.print("\n");
        printCellRow(cellRow, DaysInMonth);
    }

    private static void printMidCells(int CellRow) {
        for (int i = 0; i <= (cellLength * CalWidth); i++) {
            if (i == 0) {
                System.out.print(MidLeft);
            } else if (i == (cellLength * CalWidth)) {
                System.out.print(MidRight);
            } else if ((i % cellLength) == 0) {
                System.out.print(Centre);
            } else {
                System.out.print(Horz);
            }
        }
        System.out.print("\n");
        printCellRow(CellRow, DaysInMonth);
    }

    private static void printBotCells() {
        int CellRow = CalHeight;
        for (int i = 0; i <= (cellLength * CalWidth); i++) {
            if (i == 0) {
                System.out.print(MidLeft);
            } else if (i == (cellLength * CalWidth)) {
                System.out.print(MidRight);
            } else if ((i % cellLength) == 0) {
                System.out.print(Centre);
            } else {
                System.out.print(Horz);
            }
        }
        System.out.print("\n");

        printCellRow(CellRow, DaysInMonth);

        for (int i = 0; i <= (cellLength * CalWidth); i++) {
            if (i == 0) {
                System.out.print(BotLeft);
            } else if (i == (cellLength * CalWidth)) {
                System.out.print(BotRight);
            } else if ((i % cellLength) == 0) {
                System.out.print(BotCentre);
            } else {
                System.out.print(Horz);
            }
        }
        System.out.print("\n");
    }

    private static int getLength(String phrase) {
        return phrase.length();
    }

    private static void printCellRow(int cellRow, int DaysInMonth) {
        for (int row = 2; row < cellHeight; row++) {
            for (int i = 0; i <= cellLength * CalWidth; i++) {
                int day = getDay(i, cellRow);

                String phraseToPrint;

                if ((i % cellLength) == 0) {
                    System.out.print(Vert);
                } else if (day > DaysInMonth) {
                    phraseToPrint = shortenPhrase("");
                    System.out.print(phraseToPrint);
                    System.out.print(Vert);
                    i += cellLength;
                } else if (i == (cellLength * CalWidth)) {
                    System.out.print(Vert);
                } else if (row == 2) {
                    phraseToPrint = shortenPhrase(day);
                    System.out.print(phraseToPrint);
                    System.out.print(Vert);
                    i += cellLength;
                }

                else if ((day - 1 >= data.length) || (row - 3 > data[day - 1].length - 1)) {
                    phraseToPrint = shortenPhrase("");
                    System.out.print(phraseToPrint);
                    System.out.print(Vert);
                    i += cellLength;
                }


                else if ((row == cellHeight-1) && (data[day-1].length > cellHeight-3)) {
                    phraseToPrint = shortenPhrase("more...");
                    System.out.print(phraseToPrint);
                    System.out.print(Vert);
                    i += cellLength;
                }

                else {
                    String phrase = data[day - 1][row - 3];
                    phraseToPrint = shortenPhrase(phrase);
                    System.out.print(phraseToPrint);
                    System.out.print(Vert);
                    i += cellLength;
                }
            }
            System.out.print("\n");
        }
    }

    private static String shortenPhrase(String Phrase) {
        String result = " ";
        String padding = "";
        int length = getLength(Phrase);
        if (length <= cellLength - 2) {
            result += Phrase;
            for (int i = 0; i < cellLength - length - 3; i++) {
                padding += " ";
            }
            result += padding;
        } else { // assert need to truncate phrase
            result += Phrase.substring(0, (cellLength - 6));
            result += "...";
        }
        result += " ";
        return result;
    }

    private static String shortenPhrase(int day) {
        String result = "";
        String padding = "";
        if (day <= 9) {
            for (int i = 0; i < cellLength - 3; i++) {
                padding += " ";
            }
            result += padding + day;
        } else { // assert that day is double digit
            for (int i = 0; i < cellLength - 4; i++) {
                padding += " ";
            }
            result += padding + day;
        }
        result += " ";
        return result;
    }

    private static int getDay(int i, int cellRow) {
        int result = i / cellLength + 1 + ((cellRow - 1) * 7);
        return result;
    }

    private static void getData() throws ParseException, RimsException {
        ArrayList<ArrayList<String>> tempData = new ArrayList<ArrayList<String>>();
        for (int day = 1; day <= DaysInMonth; day++) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yyyy HHmm");
            String now = dtf.format(LocalDateTime.now());

            String strDate = day + "/" + now;

            Date date = stringToDate(strDate);
            tempData.add(getListForSpecificDay(date, resources, ui));
        }

        String[][] array = new String[tempData.size()][];
        for (int i = 0; i < tempData.size(); i++) {
            ArrayList<String> row = tempData.get(i);
            array[i] = row.toArray(new String[row.size()]);
        }
        data = array;
        //return array;
    }


    private static int getDaysInMonth(LocalDateTime date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String strDate = dtf.format(date);
        String[] ymd = strDate.split("/");
        int year = Integer.parseInt(ymd[2]);
        int month = Integer.parseInt(ymd[1]);
        int day = Integer.parseInt(ymd[0]);

        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        return daysInMonth;
    }

    public static void increaseSize() throws ParseException, RimsException {
        cellHeight ++;
        cellLength ++;
        printCal();
    }

    public static void decreaseSize() throws ParseException, RimsException {
        cellHeight --;
        cellLength --;
        printCal();
    }

    @Override
    public void execute(Ui ui, Storage storage, ResourceList resources) throws RimsException, ParseException {
        ui.formattedPrint("ITEMS LOANED OR RESERVED FOR THIS MONTH:");
        if (operator == null) {
            printCal();
        }
        else if (operator.equals("+")) {
            increaseSize();
        }
        else if (operator.equals("-")) {
            decreaseSize();
        }
    }

}
