package ru.calculat.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Павел on 21.08.2016.
 */
public class Calculate {
    static boolean noColulation = false;
    static String MuthExpression, Expression;
    static ArrayList<BigDecimal[]> arr = new ArrayList<BigDecimal[]>();
    static BigDecimal[] item;
    static BigDecimal multi = new BigDecimal("1"), div = new BigDecimal("2"), summ = new BigDecimal("3"), sub = new BigDecimal("4"), bracketL = new BigDecimal("5"), bracketR = new BigDecimal("6"), num = new BigDecimal("7"), operation, result = new BigDecimal("0"), r;
    static int indexOperation;
    public static void main(String[] args) {
        //"(5.89*(6+56))-6/0"
        MuthExpression = args[0];
        MuthExpression = MuthExpression.replaceAll("\\s", "");
        MuthExpression = getAnsuer(MuthExpression);
        System.out.print(MuthExpression);
    }

    public static String getAnsuer(String arg){
        String ans = null;
        MuthExpression = arg;
        MuthExpression = MuthExpression.replaceAll("\\s", "");
        if (interpritator()!=null){
            boolean haveIter;
            do{
                haveIter = iterator();
                if(noColulation)return null;
            } while(haveIter);
            r = arr.get(0)[1].setScale(4, BigDecimal.ROUND_HALF_UP);
            Double d = r.doubleValue();
            ans = Double.toString(d);
        }else ans = null;
        return ans;
    }

    /**
     * Нходит выражение в скобках, если есть, начиная с вложеных и отдает на вычисление
     * @return
     */
    public  static boolean iterator(){
        boolean breackR = false, breack = false, haveIter = false;
        int size = arr.size(), brackStart = -1, brakeStop = -1;
        for (int i=0; i < size; i++){
            if (arr.get(i)[0]==bracketL){
                brackStart = i;
                breack = true;
                noColulation = true;
            }else if (arr.get(i)[0]==bracketR){
                if(!breackR){
                    brakeStop = i;
                    noColulation = false;
                }
            }
        }
        if (breack){
            getResultExpression(brackStart+1, brakeStop-1);
            arr.remove(brackStart+2);
            arr.remove(brackStart);
            haveIter = true;
        }else if(size>1) {
            if (getResultExpression(0, size-1))haveIter = true;
            else haveIter = false;
        }
        return haveIter;
    }

    /**
     * Вычисляет целые выражения без скобак и записывает в масив, возвращает false при делении на ноль
     * @param start
     * @param stop
     * @return
     */
    public  static boolean getResultExpression (int start, int stop){
        boolean haveOperation = haveOperation(start, stop);
        while (haveOperation){
            result = calculateOperation(arr.get(indexOperation-1)[1], arr.get(indexOperation+1)[1], operation);
            if(noColulation)return false;
            rewriteArr(indexOperation-1, indexOperation+1, new BigDecimal[]{num, result});
            stop -= 2;
            haveOperation = haveOperation(start, stop);
        }
        return true;
    }

    /**
     * Проверяет нужно ли вычислять данный участок выражения
     * @param start
     * @param stop
     * @return
     */
    public  static boolean haveOperation(int start, int stop){
        boolean haveOperation = false;
        boolean haveMulti = false, haveDivi = false, haveSumm = false, haveSub = false;
        int indexMulti = 0, indexDiv = 0, indexSumm = 0, indexSub = 0;
        for (int i=start; i < stop; i++){
            if (arr.get(i)[0]==multi){
                haveMulti = true;
                indexMulti = i;
            }else if (arr.get(i)[0]==div){
                haveDivi = true;
                indexDiv = i;
            }else if (arr.get(i)[0]==summ){
                haveSumm = true;
                indexSumm = i;
            }else if (arr.get(i)[0]==sub){
                haveSub = true;
                indexSub = i;
            }
        }
        if(haveMulti){
            operation = multi;
            indexOperation = indexMulti;
            haveOperation = true;
        } else if(haveDivi){
            operation = div;
            indexOperation = indexDiv;
            haveOperation = true;
        }else if(haveSumm){
            operation = summ;
            indexOperation = indexSumm;
            haveOperation = true;
        }else if(haveSub){
            operation = sub;
            indexOperation = indexSub;
            haveOperation = true;
        }
        return haveOperation;
    }

    /**
     * Производит простые операции с цифрами и возвращает результат
     * @param a переменная для вычисления
     * @param b переменная для вычисления
     * @param operation тип операции
     * @return
     */
    public  static BigDecimal calculateOperation (BigDecimal a, BigDecimal b, BigDecimal operation){
        BigDecimal result = new BigDecimal("0");
        if (operation==multi){
            result = a.multiply(b);
        }else if(operation==div){
            if (b.signum()!=0){
                result = a.divide(b);
            }else noColulation = true;
        }else if(operation==summ){
            result = a.add(b);
        }else if(operation==sub){
            result = a.subtract(b);
        }
        return result;
    }

    /**
     * Заменяет вычесленный участок на вычесленное значение
     * @param start начало участка
     * @param stop конец участка
     * @param set вычесленное значение
     */
    public static void rewriteArr(int start, int stop, BigDecimal set[]){
        for(int i = stop; i+1 > start; i--){
            if(i==start){
                arr.set(i, set);
            }else {
                arr.remove(i);
            }
        }
    }

    /**
     * Преобразует  строку в ArrayList<BigDecimal[]>
     *     где BigDecimal[]{код типа данных ( *-1, /-2, +-3, --4,(-5, )-6, num-7) , число или 0};
     */
    public static String interpritator(){
        int length = MuthExpression.length();
        boolean isDigit =false, first = true;
        BigDecimal digit = new BigDecimal("0");
        String digital = null;
        for(int i = 0; i<length; i++){
            String chare = MuthExpression.substring(i, i+1);
            Character charect = MuthExpression.charAt(i);
            if (Character.isDigit(charect)||chare.equals(".")){
                if(first){
                    digital = chare;
                }else digital += chare;
                first = false;
                isDigit = true;
            }else if(isDigit&&!Character.isDigit(charect)&&!chare.equals(".")){
                digit = new BigDecimal(digital);
                arr.add(new BigDecimal[]{num, digit});
                isDigit = false;
                first = true;
                digital = null;
            }
            if(chare.equals("(")){
                arr.add(new BigDecimal[]{bracketL, new BigDecimal("0")});
            }else if(chare.equals(")")){
                arr.add(new BigDecimal[]{bracketR, new BigDecimal("0")});
            }else if(chare.equals("*")){
                arr.add(new BigDecimal[]{multi, new BigDecimal("0")});
            }else if(chare.equals("/")){
                arr.add(new BigDecimal[]{div, new BigDecimal("0")});
            }else if(chare.equals("+")){
                arr.add(new BigDecimal[]{summ, new BigDecimal("0")});
            }else if(chare.equals("-")) {
                arr.add(new BigDecimal[]{sub, new BigDecimal("0")});
            }else if (!isDigit){
                return null;
            }
        }
        if(isDigit){
            digit = new BigDecimal(digital);
            arr.add(new BigDecimal[]{num, digit});
        }
        return "";
    }
}
