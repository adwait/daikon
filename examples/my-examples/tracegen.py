import sys

w_seq = [4, 3, 2, 1, 2, 4, -1, 4, 1, 98, 12, 1, 4, 5, 4, 54]
x_seq = [1, 4, 3, 2, 4, 4, 1, 2, 1, -23, 1, 1, 8, 3, 1, 4]
y_seq = [4, 1, 3, 1, -3, 1, 2, 1, 1, 8, 9, 12, 6, 2, 4, 6]

w_seq = [4, 2, 2, 2, 2, 4, 2, 4, 42, 98, 12, 8, 2, 10, 52, 2, 4, 2]
x_seq = [12, 4, 34, 2, 4, 4, 6, 2, 12, 22, 12, 10, 6, 2,2, 2, 32, 2]
y_seq = [4, 1, 3, 1, 3, 2, 2, 2, 2, 8, 94, 12, 52, 6, 4, 6, 8, 4]

# w_seq = [8, 4, 1, 21]
# x_seq = [3, 4, 4, 20]
# y_seq = [3, 6, 2, 21]


def create_enter_block (wv, xv, yv, zv) -> str:
    return '''
..fun1():::ENTER
::w
{}
1
::x
{}
1
::y
{}
1
::z
{}
2
'''.format(wv, xv, yv, zv)


def create_exit_block (wv, xv, yv, zv) -> str:
    return '''
..fun1():::EXIT0
::w
{}
1
::x
{}
1
::y
{}
1
::z
{}
1
'''.format(wv, xv, yv, zv)


def create_preamble ():
    return '''
input-language Uclid
decl-version 2.0
var-comparability implicit
'''

def main(args):
    filename = args[0]
    print(filename)
    with open(filename, 'w') as fileh:
        fileh.write(create_preamble())

        for i in range(len(w_seq)):
            wv, xv, yv = w_seq[i], x_seq[i], y_seq[i]
            fileh.write(create_enter_block(wv, xv, yv, 'nonsensical'))
            fileh.write(create_exit_block(wv, xv, yv, wv+xv+yv))


if __name__ == '__main__':
    main(sys.argv[1:])