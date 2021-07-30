import sys

# w_seq = [4, 3, 2, 1, 2, 4, 1, 4, 1, 98, 12, 1]
# x_seq = [1, 4, 3, 2, 4, 4, 1, 2, 1, 23, 1, 1]
# y_seq = [4, 1, 3, 1, 3, 1, 2, 1, 1, 8, 9, 12]

w_seq = [4, 2, 2, 2, 2, 4, 2, 4, 42, 98, 12, 8]
x_seq = [12, 4, 34, 2, 4, 4, 6, 2, 12, 22, 12, 10]
y_seq = [4, 1, 3, 1, 3, 1, 2, 1, 1, 8, 9, 12]


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

filename = 'sample1.dtrace'

with open(filename, 'w') as fileh:
    fileh.write(create_preamble())

    for i in range(len(w_seq)):
        wv, xv, yv = w_seq[i], x_seq[i], y_seq[i]
        fileh.write(create_enter_block(wv, xv, yv, 'nonsensical'))
        fileh.write(create_exit_block(wv, xv, yv, wv+xv+yv))

