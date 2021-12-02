data = map(str.split, open("inputs/02.txt").readlines())
x = y = aim = 0

for command, value in data:
    value = int(value)
    if command == 'forward':
        x += value
        y += aim * value
    elif command == 'down':
        aim += value
    elif command == 'up':
        aim -= value

print(x*aim, x*y)
