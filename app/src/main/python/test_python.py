import sys
import math
import random
from datetime import datetime

def test_basic_operations():
    """测试基础运算"""
    a, b = 10, 20
    return {
        "add": a + b,
        "subtract": b - a,
        "multiply": a * b,
        "divide": b / a if a != 0 else "Error: Division by zero"
    }

def test_string_operations():
    """测试字符串操作"""
    text = "Hello, Python!"
    return {
        "length": len(text),
        "uppercase": text.upper(),
        "lowercase": text.lower(),
        "split": text.split(", ")
    }

def test_list_operations():
    """测试列表操作"""
    numbers = [1, 2, 3, 4, 5]
    return {
        "sum": sum(numbers),
        "max": max(numbers),
        "min": min(numbers),
        "reverse": numbers[::-1],
        "sorted_desc": sorted(numbers, reverse=True)
    }

def test_math_operations():
    """测试数学运算"""
    return {
        "pi": math.pi,
        "sqrt_16": math.sqrt(16),
        "sin_90": math.sin(math.radians(90)),
        "random": random.uniform(1, 100)
    }

def test_datetime_operations():
    """测试日期时间"""
    now = datetime.now()
    return {
        "current_time": now.strftime("%Y-%m-%d %H:%M:%S"),
        "timestamp": now.timestamp(),
        "is_weekday": now.weekday() < 5
    }

def test_error_handling():
    """测试错误处理"""
    try:
        result = 10 / 0
    except Exception as e:
        return {"error": str(e)}
    return {"result": result}

def run_all_tests():
    """运行所有测试"""
    tests = {
        "basic_operations": test_basic_operations(),
        "string_operations": test_string_operations(),
        "list_operations": test_list_operations(),
        "math_operations": test_math_operations(),
        "datetime_operations": test_datetime_operations(),
        "error_handling": test_error_handling()
    }
    return tests

if __name__ == "__main__":
    # 执行所有测试并打印结果
    results = run_all_tests()
    print("=== 测试结果 ===")
    for test_name, result in results.items():
        print(f"\n{test_name}:")
        if isinstance(result, dict):
            for key, value in result.items():
                print(f"  {key}: {value}")
        else:
            print(f"  {result}")