Image source:
// test Seattle image file
// from http://travel.nationalgeographic.com/travel/city-guides/seattle-photos-2/#/
// seattle-queen-anne_2487_600x450.jpg

// test Boston image file from
// http://wheatoncollege.edu/about/our-location/boston/

Image Files:
https://drive.google.com/file/d/0Bx2vnEKuTknbYjFEZk54LTJ6cjQ/view?usp=sharing

Attempting to compress the images with ratios below 40% produced relatively slight differences to both the image’s appearance and its file size. As we increase the compression ratio, the visual and file-size changes become much more pronounced. By 90%, there are noticeable artifacts and an apparent blockiness to the photos. By 99%, the image becomes very obviously blurred and distorted. File size also seems to decrease more rapidly in the 90-99% range than it did for lower compression ratios.

In most cases, the ideal outcome is likely to maximize the compression (minimize file size) while also having as little impact possible on the quality of the image. For our test images, for example: Seattle.png. We found ratios of around 70-80% to produce a good balance between size reduction and lack of noticeable image degradation.