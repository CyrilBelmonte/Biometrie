#!/usr/bin/env python

from PIL import Image
from skimage.io import imread, imshow

import cv2
import matplotlib
import matplotlib.pyplot as plt
from build_reference_table import *
import sys

from match_table import *
from find_maxima import *

import numpy as np


def point(model, image):
    refim = imread(model)
    im = imread(image)

    table = buildRefTable(refim)
    acc = matchTable(im, table)
    val, ridx, cidx = findMaxima(acc)

    # code for drawing bounding-box in accumulator array...

    val = int(val)
    ridx = int(ridx)
    cidx = int(cidx)

    acc[ridx - 2:ridx + 2, cidx - 2] = val
    acc[ridx - 2:ridx + 2, cidx + 2] = val

    acc[ridx - 2, cidx - 2:cidx + 2] = val
    acc[ridx + 2, cidx - 2:cidx + 2] = val

    plt.figure(1)
    imshow(acc)
    plt.show()
    plt.savefig('./generate/Acc.png')

    # code for drawing bounding-box in original image at the found location...

    # find the half-width and height of template
    hheight = np.floor(refim.shape[0] / 2) - 1
    hwidth = np.floor(refim.shape[1] / 2) - 1

    # find coordinates of the box
    rstart = int(max(ridx - hheight, 1))
    rend = int(min(ridx + hheight, im.shape[0] - 1))
    cstart = int(max(cidx - hwidth, 1))
    cend = int(min(cidx + hwidth, im.shape[1] - 1))

    # draw the box
    im[rstart:rend, cstart] = 255
    im[rstart:rend, cend] = 255

    im[rstart, cstart:cend] = 255
    im[rend, cstart:cend] = 255

    # show the image
    plt.figure(2), imshow(refim)
    plt.savefig('./generate/Modele.png')
    plt.figure(3), imshow(im)
    plt.savefig('./generate/Images.png')
    plt.show()
    plt.savefig('./generate/details_hough.png')
    return ridx, cidx


def imageLocal(ridx, cidx):
    # Download Image:
    im = Image.open("Modele/pictures.png")
    # Check Image Size
    im_size = im.size
    # Define box inside image
    left = ridx
    top = cidx
    width = 100
    height = 100
    # Create Box
    box = (left, top, left + width, top + height)
    # Crop Image
    area = im.crop(box)
    # Save Image
    area.save("./generate/localImage.png")


def histogram(image):
    img = cv2.imread(image)
    return cv2.calcHist([img], [0], None, [256], [0, 256])


image = sys.argv[1]

model = './Modele/Cercle.png'

ridx, cidx = point(model, image)

imageLocal(ridx, cidx)

print(histogram('./generate/localImage.png'))
