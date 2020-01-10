#!/usr/bin/env python

import numpy as np
from scipy.ndimage import convolve
from skimage import io
import matplotlib.pyplot as plt


def buildRefTable(img):
    table = [[0 for x in range(1)] for y in range(90)]  # creating a empty list
    # r will be calculated corresponding to this point
    img_center = [int(img.shape[0] / 2), int(img.shape[1] / 2)]

    def findAngleDistance(x1, y1):
        x2, y2 = img_center[0], img_center[1]
        r = [(x2 - x1), (y2 - y1)]
        if (x2 - x1 != 0):
            return [int(np.rad2deg(np.arctan(int((y2 - y1) / (x2 - x1))))), r]
        else:
            return [0, 0]

    filter_size = 3

    for x in range(img.shape[0] - (filter_size - 1)):
        for y in range(img.shape[1] - (filter_size - 1)):
            if (img[x, y].all() != 0):
                theta, r = findAngleDistance(x, y)
                if (r != 0):
                    table[np.absolute(theta)].append(r)

    for i in range(len(table)):
        table[i].pop(0)

    return table
