import sys

class StringBuilder:
    def __init__(self):
        self.lines = []

    def write(self, s):
        self.lines.append(s)

    def flush(self):
        pass

    def get(self):
        return ''.join(self.lines)

def redirect_stdout():
    sb = StringBuilder()
    sys.stdout = sb
    return sb