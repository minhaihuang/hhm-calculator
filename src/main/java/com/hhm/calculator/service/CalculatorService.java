package com.hhm.calculator.service;

import com.hhm.calculator.dto.CalResultDto;
import com.hhm.calculator.util.MyCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author huanghm
 * @Date 2023/3/19
 */
@Service
public class CalculatorService {
    // 这样声明会有线程问题，由于只是演示demo，暂不处理
    private static List<String> expressionList = new ArrayList<>();
    private static int nowIndex;
    // 防止list无限增大，最多只保存10条记录。
    private static final int EXPRESSION_SIZE = 10;

    public CalResultDto calculate(String expression) {
        String result = String.valueOf(MyCalculator.calculate(expression));
        result = result.endsWith(".0")? result.substring(0, result.length() -2): result;

        if (expressionList.size() >= EXPRESSION_SIZE) {
            expressionList.remove(0);
        }
        expressionList.add(expression);
        nowIndex = expressionList.size() - 1;
        return new CalResultDto(expression,result);
    }

    public CalResultDto undo() {
        if(expressionList.size() == 0) {
            return new CalResultDto("","0");
        }

        nowIndex = nowIndex == 0? 0: nowIndex - 1;

        String expression = expressionList.get(nowIndex);
        String result = String.valueOf(MyCalculator.calculate(expression));
        result = result.endsWith(".0")? result.substring(0, result.length() -2): result;
        return new CalResultDto(expression,result);
    }

    public CalResultDto redo() {
        if(expressionList.size() == 0) {
            return new CalResultDto("","0");
        }
        nowIndex = nowIndex == expressionList.size() - 1? nowIndex: nowIndex + 1;
        String expression = expressionList.get(nowIndex);
        String result = String.valueOf(MyCalculator.calculate(expression));
        result = result.endsWith(".0")? result.substring(0, result.length() -2): result;
        return new CalResultDto(expression,result);
    }

    public CalResultDto clearHistory() {
        expressionList.clear();
        nowIndex = 0;
        return new CalResultDto("",String.valueOf(0));
    }
}
