"""check
>>> print float(float_num)
0.8476"""
str = "X-DSPAM-Confidence:  0.8475"
lookup=str.find(':')
float_num=(str[lookup+1:]).strip()
print float(float_num)
2733433