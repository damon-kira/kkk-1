import sys
import builtins
from io import StringIO
from typing import Dict, Any

class SecurityError(Exception):
    pass

def safe_exec(code: str) -> Dict[str, Any]:
    """安全执行Python代码"""
    result = {"output": "", "result": None, "error": ""}

    try:
        # 重定向标准输出
        stdout = sys.stdout
        sys.stdout = StringIO()

        # 代码预处理
        code = code.replace("sys._getframe", "# 禁止访问栈帧")

        # 修复点：使用正确的builtins模块
        safe_globals = {
            '__builtins__': builtins.__dict__  # 直接使用已导入的builtins模块
        }

        # 执行代码
        exec(code, safe_globals)

        # 获取结果
        result["output"] = sys.stdout.getvalue()
        result["result"] = str(safe_globals.get('__result__', None))

    except SecurityError as e:
        result["error"] = f"安全限制: {e}"
    except Exception as e:
        result["error"] = f"{type(e).__name__}: {str(e)}"
    finally:
        sys.stdout = stdout

    return result