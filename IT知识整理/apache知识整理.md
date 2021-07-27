# Content-Security-Policy, X-Content-Type-Options, X-XSS-Protection 漏洞

httpd.conf添加

```properties
<IfModule mod_headers.c> 
#Header set Content-Security-Policy: default-src'self';img-src'self'https://www.onx8.com;object-src'none';script-src'self';style-src'self';frame-ancestors'self';base-uri'self';form-action'self';
Header set Content-Security-Policy "style-src 'self' 'unsafe-inline' ;"
# Header always append X-Frame-Options SAMEORIGIN
#Header set Access-Control-Allow-Origin: https://www.onx8.com
# Header set X-XSS-Protection: 1;mode=block
Header always set X-XSS-Protection "1;  mode=block"
Header set X-Content-Type-Options: nosniff
#Header set Strict-Transport-Security: max-age=31536000;includeSubDomains
</IfModule>
```

