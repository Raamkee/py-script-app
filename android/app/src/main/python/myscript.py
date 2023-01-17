from os.path import dirname, join

def main():
    filename = join(dirname(__file__), "/data/user/0/com.example.py_script_app/app_flutter/my_file.txt")
    file1 = open("my_file.txt","r+")
    return "Hello World..."