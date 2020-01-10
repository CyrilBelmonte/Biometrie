#!/usr/bin/env python
import numpy as np
from build_reference_table import *


def matchTable(im, table):
    m, n = im.shape[:-1]
    acc = np.zeros((m, n))

    def findGradient(x, y):
        if (x != 0):
            return int(np.rad2deg(np.arctan(int(y / x))))
        else:
            return 0

    for x in range(100, im.shape[0] - 200):
        for y in range(20, im.shape[1] - 20):
            if im[x, y].all() != 0:
                theta = findGradient(x, y)
                vectors = table[theta]
                for vector in vectors:
                    acc[vector[0] + x, vector[1] + y] += 1
    return acc
