# Complete Karaf Deployment Guide

## Step 1: Stop Karaf (if running)

In Karaf console, type:

```
shutdown
```

## Step 2: Clean Karaf Cache

Navigate to Karaf directory and delete cache:

```powershell
cd C:\Users\User\Downloads\apache-karaf-4.4.4
Remove-Item -Recurse -Force .\data
```

## Step 3: Start Karaf

```powershell
.\bin\karaf.bat
```

## Step 4: Install Bundles in Karaf Console

```
bundle:install -s file:c:/Users/User/Desktop/github/openolat-osgi/common-api/target/common-api-1.0.0-SNAPSHOT.jar
bundle:install -s file:c:/Users/User/Desktop/github/openolat-osgi/enrollment-component/target/enrollment-component-1.0.0-SNAPSHOT.jar
```

## Step 5: Verify Deployment

```
bundle:list
scr:list
log:tail
```

## Alternative: Use feature:install

If the above doesn't work, you can also install required SCR support:

```
feature:install scr
```

Then retry installing the bundles.
