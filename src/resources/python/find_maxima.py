#!/usr/bin/env python

import numpy as np


def findMaxima(acc):
    ridx, cidx = np.unravel_index(acc.argmax(), acc.shape)
    return [acc[ridx, cidx], ridx, cidx]
