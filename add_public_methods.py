#!/usr/bin/env python3
import re
import glob

def add_public_method(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Extract command name
    command_match = re.search(r'@Command\(scope = "(\w+)", name = "([^"]+)"', content)
    if not command_match:
        return
    
    name = command_match.group(2)
    function_name = name.replace('-', '_')
    
    # Extract all @Argument fields
    arguments = []
    for match in re.finditer(r'@Argument\(index = (\d+),.*?\)\s+private (\w+) (\w+);', content, re.DOTALL):
        index = int(match.group(1))
        type_name = match.group(2)
        var_name = match.group(3)
        arguments.append((index, type_name, var_name))
    
    # Sort by index
    arguments.sort(key=lambda x: x[0])
    
    # Build public method
    params = ', '.join([f'{t} {v}' for _, t, v in arguments])
    assignments = '\n        '.join([f'this.{v} = {v};' for _, _, v in arguments])
    
    if assignments:
        method = f'''
    public void {function_name}({params}) throws Exception {{
        {assignments}
        execute();
    }}
'''
    else:
        method = f'''
    public void {function_name}() throws Exception {{
        execute();
    }}
'''
    
    # Insert method before @Override
    content = re.sub(
        r'(\n    @Override\n    public Object execute)',
        method + r'\1',
        content
    )
    
    with open(filepath, 'w') as f:
        f.write(content)
    
    print(f"Added method: {function_name}({params if params else ''})")

command_files = glob.glob('/Users/lichee/Documents/GitHub/openolat-osgi/course-component/src/main/java/my/um/cbse/course/command/*Command.java')

for filepath in command_files:
    add_public_method(filepath)

print("\nDone!")
