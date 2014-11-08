set CONVERT=f:\bin32\imagemagick-6.7.2-10\convert 

%CONVERT% %1 -resize 16x16 16.png
%CONVERT% %1 -resize 32x32 32.png
%CONVERT% %1 -resize 48x48 48.png
%CONVERT% %1 -resize 64x64 64.png
