#!/usr/bin/env python

import cv2
import matplotlib.pyplot as plt
import numpy as np
import sys
from PIL import Image
from build_reference_table import *
from find_maxima import *
from match_table import *
from skimage.io import imread, imshow


def point(model, image):
    refim = imread(model)
    im = imread(image)

    table = buildRefTable(refim)
    acc = matchTable(im, table)

    val, ridx, cidx = findMaxima(acc)

    val = int(val)
    ridx = int(ridx)
    cidx = int(cidx)

    acc[ridx - 2:ridx + 2, cidx - 2] = val
    acc[ridx - 2:ridx + 2, cidx + 2] = val
    acc[ridx - 2, cidx - 2:cidx + 2] = val
    acc[ridx + 2, cidx - 2:cidx + 2] = val

    hheight = np.floor(refim.shape[0] / 2) - 1
    hwidth = np.floor(refim.shape[1] / 2) - 1
    rstart = int(max(ridx - hheight, 1))
    rend = int(min(ridx + hheight, im.shape[0] - 1))
    cstart = int(max(cidx - hwidth, 1))
    cend = int(min(cidx + hwidth, im.shape[1] - 1))

    im[rstart:rend, cstart] = 255
    im[rstart:rend, cend] = 255

    im[rstart, cstart:cend] = 255
    im[rend, cstart:cend] = 255

    plt.figure(1), imshow(acc)
    plt.savefig('./src/resources/python/generate/Acc.png')
    plt.figure(2), imshow(im)
    plt.savefig('./src/resources/python/generate/details_hough.png')
    return ridx, cidx


def imageLocal(ridx, cidx):
    im = Image.open("./src/resources/tmp/pictures.png")
    im_size = im.size

    left = cidx - 170
    top = ridx + 5
    width = 50
    height = 50

    box = (left, top, left + width, top + height)
    area = im.crop(box)
    area.save("./src/resources/python/generate/localImage.png")


def histogramMoy(image):
    img = cv2.imread(image)
    img_t = cv2.calcHist([img], [0], None, [256], [0, 256])
    return np.mean(img_t)


def histogram(image):
    img = cv2.imread(image)
    tmp = ""

    imageTmp = list(cv2.calcHist([img], [0], None, [256], [0, 256]))

    for a in imageTmp:
        tmp = tmp + str(int(list(a)[0])) + ","

    tmp = tmp[:-1]

    return tmp


image = sys.argv[1]

model = './src/resources/python/Modele/Cercle.png'

ridx, cidx = point(model, image)

imageLocal(ridx, cidx)

print(histogram('./src/resources/python/generate/localImage.png'))
