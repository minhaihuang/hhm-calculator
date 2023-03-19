package com.hhm.calculator.controller;

import com.hhm.calculator.dto.CalResultDto;
import com.hhm.calculator.service.CalculatorService;
import com.hhm.calculator.util.MyCalculator;
import com.hhm.calculator.util.Result;
import com.hhm.calculator.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author huanghm
 * @Date 2023/3/19
 */
@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private static Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    @Resource
    private CalculatorService calculatorService;
    @GetMapping("/cal")
    public Result<CalResultDto> cal(@RequestParam("expression") String expression) {
        logger.info(expression);
        try {
            return ResultUtil.success(calculatorService.calculate(expression));
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            return ResultUtil.fail(new CalResultDto(expression, "NAN"));
        }
    }

    @GetMapping("/undo")
    public Result<CalResultDto> undo() {
        return ResultUtil.success(calculatorService.undo());
    }

    @GetMapping("/redo")
    public Result<CalResultDto> redo() {
        return ResultUtil.success(calculatorService.redo());
    }

    @GetMapping("/clearHistory")
    public Result<CalResultDto> clearHistory() {
        return ResultUtil.success(calculatorService.clearHistory());
    }
}
