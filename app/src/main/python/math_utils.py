import numpy as np

def add(a, b):
    """加法示例"""
    return a + b

def calculate_stats(data):
    """使用 numpy 计算统计量"""
    arr = np.array(data)
    return {
        "mean": float(np.mean(arr)),
        "max": int(np.max(arr)),
        "min": int(np.min(arr))
    }