import os
import codecs
import chardet

def convert_encoding(filepath, target_encoding='utf-8'):
    """
    将文件转换为目标编码
    :param filepath: 文件路径
    :param target_encoding: 目标编码，默认为utf-8
    """
    # 检测文件编码
    with open(filepath, 'rb') as f:
        raw_data = f.read()
        detected = chardet.detect(raw_data)
        original_encoding = detected['encoding']
        
        # 如果检测不到编码或已经是目标编码，则跳过
        if not original_encoding or original_encoding.lower() == target_encoding.lower():
            return False
        
        # 尝试用检测到的编码读取内容
        try:
            content = raw_data.decode(original_encoding)
        except UnicodeDecodeError:
            # 如果检测编码失败，尝试用GBK读取
            try:
                content = raw_data.decode('gbk')
            except UnicodeDecodeError:
                print(f"无法解码文件: {filepath}")
                return False
        
        # 写入目标编码
        try:
            with codecs.open(filepath, 'w', encoding=target_encoding) as f:
                f.write(content)
            return True
        except IOError as e:
            print(f"写入文件失败: {filepath}, 错误: {e}")
            return False

def convert_directory(directory='.', extensions=None):
    """
    转换目录下所有指定扩展名的文件
    :param directory: 目录路径，默认为当前目录
    :param extensions: 要转换的文件扩展名列表，None表示所有文件
    """
    converted_count = 0
    for root, dirs, files in os.walk(directory):
        for filename in files:
            if extensions is not None:
                if not any(filename.endswith(ext) for ext in extensions):
                    continue
            
            filepath = os.path.join(root, filename)
            try:
                if convert_encoding(filepath):
                    print(f"已转换: {filepath}")
                    converted_count += 1
            except Exception as e:
                print(f"处理文件出错: {filepath}, 错误: {e}")
    
    print(f"\n转换完成! 共转换了 {converted_count} 个文件")

if __name__ == '__main__':
    import argparse
    
    parser = argparse.ArgumentParser(description='将GBK编码文件转换为UTF-8编码')
    parser.add_argument('-d', '--directory', default='.', help='要处理的目录，默认为当前目录')
    parser.add_argument('-e', '--extensions', nargs='+', default=None, 
                       help='要处理的文件扩展名列表，如.txt .java等')
    
    args = parser.parse_args()
    
    print(f"开始处理目录: {os.path.abspath(args.directory)}")
    if args.extensions:
        print(f"只处理以下扩展名的文件: {', '.join(args.extensions)}")
    
    convert_directory(args.directory, args.extensions)