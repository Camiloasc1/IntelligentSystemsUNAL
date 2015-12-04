import random

for _ in xrange(100):
    pat = ""
    # board = []
    pat += "("
    count = []
    for i in xrange(4):
    #     board.append([])
        c = 0
        for j in xrange(8):
            cell = random.random() < 0.4
            c += 1 if cell else 0
            pat += "0.9 " if cell else "0.1 "
    #         board[i].append(cell)
        count.append(c)
    # for j in xrange(8):
    #     for i in xrange(4):
    #         print "X" if board[i][j] else "O",
    #     print
    # print count
    m = min(count)
    for i in count:
        pat += "0.9 " if m == i else "0.1 "
    pat = pat[:-1] + ")\n"
    print pat,
