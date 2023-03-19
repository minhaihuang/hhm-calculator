package com.hhm.calculator.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 操作数栈：解析表达式，采用逐个读取字符的形式，如果是操作数就入操作数栈，需要注意的是，我们是逐个读取字符，也就意味着数字"123"将分3次读取("1","2","3")，
 *         所以需要一个追加器将字符缓存起来，当完整的读取了整个数值时在入操作数栈。
 * 运算符栈：如果是运算符就进行比较，优先级同级或低于栈顶运算符时，将触发一次二元运算，进行二元运算的为运算符栈的栈顶元素和操作数栈顶的两个元素，再把运算得到的结果入操作数栈。
 *         如果运算栈还有元素，这是一个递归操作，直到运算栈没有元素或者运算符的优先级高于栈顶元素，那么把运算符入运算符栈。继续读取表达式的下一个元素。
 * @Author huanghm
 * @Date 2023/3/19
 */
public class MyCalculator {
    private static final Map<String, Integer> OP_LEVEL_MAP = new HashMap<>();
    static {
        // 加减同级，乘除同级，乘除比加减级别高
        OP_LEVEL_MAP.put("(", 0);
        OP_LEVEL_MAP.put("+", 1);
        OP_LEVEL_MAP.put("-", 1);
        OP_LEVEL_MAP.put("/", 2);
        OP_LEVEL_MAP.put("*", 2);
        OP_LEVEL_MAP.put(")", 3);
    }
    public static void main(String[] args) {
        String expression = "1+2*(3-1)";
        double res = calculate(expression);
        System.out.println(res);
    }

    public static double calculate(String expression) {
        // 装数字的stack，可包含小数
        Stack<BigDecimal> decimalStack = new Stack<>();
        // 装运算符的stack，可包含 + - * / ( )
        Stack<String> opStack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            final char c = expression.charAt(i);
            // 第一个数字允许为负数
            if (c == '-' && i == 0) {
                sb.append(c);
            }else if (c >= '0' && c <= '9' || c == '.') { // 可以为整数或者小数
                sb.append(c);
            }else {
                if (sb.length() > 0) {
                    decimalStack.push(new BigDecimal(sb.toString()));
                    sb.delete(0, sb.length());
                }
                // 当运算符比运算符栈的栈顶元素优先级低时，循环对操作数栈的两个栈顶元素进行二元运算，直到运算符跟栈顶元素同级
                String op = String.valueOf(c);
                if (opStack.empty()) {
                    opStack.push(op);
                }else {
                    //如果是"("则直接入运算栈
                    if ("(".equals(op)){
                        opStack.push(op);
                    }else if (")".equals(op)){
                        //如果是")"则进行括号匹配运算括号内的表达式
                        while (!"(".equals(opStack.peek())){
                            doCal(decimalStack,opStack);
                        }
                        opStack.pop();
                    }else {
                        while (!opStack.empty()) {
                            if (isLevelLowerOrSame(op, opStack.peek())) {
                                // 对操作数栈的两个栈顶元素进行二元运算
                                doCal(decimalStack, opStack);
                                // 都算完成后，把本次的运算符记录
                                if(opStack.empty()) {
                                    opStack.push(op);
                                    break;
                                }
                            }else{
                                opStack.push(op);
                                break;
                            }
                        }
                    }
                }
            }
        }
        // 处理最后一个数字
        if (sb.length() > 0 ){
            decimalStack.push(new BigDecimal(sb.toString()));
        }

        while (!opStack.empty()){
            doCal(decimalStack,opStack);
        }
        return decimalStack.pop().doubleValue();
    }

    public static void doCal(Stack<BigDecimal> decimalStack, Stack<String> opStack) {
        BigDecimal b2 = decimalStack.pop();
        BigDecimal b1 = decimalStack.pop();
        String op = opStack.pop();
        BigDecimal result = new BigDecimal(0);
        switch (op){
            case "+":
                result = b1.add(b2);
                break;
            case "-":
                result = b1.subtract(b2);
                break;
            case "*":
                result = b1.multiply(b2);
                break;
            case "/":
                result = b1.divide(b2,7, BigDecimal.ROUND_HALF_UP);
                break;
            default:
                break;
        }
        decimalStack.push(result);
    }


    /**
     * 相同或低级返回true，高级则返回false
     * @param targetOp
     * @param compareOp
     * @return
     */
    private static boolean isLevelLowerOrSame(String targetOp, String compareOp) {
        return OP_LEVEL_MAP.get(targetOp) <= OP_LEVEL_MAP.get(compareOp);
    }
}
