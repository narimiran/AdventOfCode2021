from aoc import read_input_line, integers


def move(x, y, vx, vy):
    x += vx
    y += vy
    if vx > 0: vx -= 1
    vy -= 1
    return x, y, vx, vy


def will_hit(vx, vy):
    x, y = 0, 0
    while x <= x2 and y >= y1:
        x, y, vx, vy = move(x, y, vx, vy)
        if x1 <= x <= x2 and y1 <= y <= y2:
            return True
    return False


x1, x2, y1, y2 = integers(read_input_line(17))

print(y1 * (y1+1) // 2)
print(sum(will_hit(vx, vy)
          for vx in range(x2+1)
          for vy in range(y1, -y1)))
