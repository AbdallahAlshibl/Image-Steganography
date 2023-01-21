# Image-Steganography
there are two java files, one for encode and one for decode.

IS_encode file (to encode):
- this java file will take text file and image as inputs, and will output image 
that has the encoded text within.

- you need to change the paths for three files,
MESSAGEFILE -> the text file that need to encode in image.
INPUTIMAGE -> the input image that will encode the text within.
OUTPUTIMAGE -> the output image that has the encode text -> the image that 
used in the decode class.

------------------------------------------------

IS_decode file (to decode):
- this java file will take the output image of (IS_encode) as an input.
- this java file will output a new text file that has the decoded text.

- you need to change the paths for two files,
INPUTIMAGE -> the input image that need to be decoded the get the text.
DECODEDMESSAGEFILE -> the path for the new file that has the decoded text (create new file).
