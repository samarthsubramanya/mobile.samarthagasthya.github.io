# Vercel Deployment Guide

This project is a Kotlin/JS static site. Vercel should build it with the Gradle wrapper and serve the generated `dist` directory.

## Current Project Settings

The repository already includes `vercel.json` with the important deployment settings:

- Build Command: `npm run build`
- Output Directory: `dist`
- SPA routing: all routes rewrite to `/`
- Static asset cache headers for JavaScript, images, and other resources

The `npm run build` script runs `./gradlew syncMobileDist`, which compiles the Kotlin/JS app and syncs the production output into `dist`.

## Deploy With GitHub

1. Push the repository to GitHub.
2. Open the Vercel dashboard and choose **Add New > Project**.
3. Import this repository.
4. Set the project root to this directory if the repository is inside a larger workspace.
5. Keep the framework preset as **Other** if Vercel does not detect one.
6. Confirm these build settings:
   - Build Command: `npm run build`
   - Output Directory: `dist`
   - Install Command: leave as Vercel default unless you need to force `npm install`
7. Click **Deploy**.

After the first deploy, every push to the production branch creates a production deployment. Pull requests and non-production branches create preview deployments.

## Deploy With Vercel CLI

Install and log in:

```bash
npm install -g vercel
vercel login
```

From this project directory, create or link the Vercel project:

```bash
vercel
```

Deploy to production:

```bash
vercel --prod
```

## Local Verification

Before deploying, run:

```bash
npm run build
```

The generated site should be in `dist`.

## Portfolio Data Source

The app loads portfolio data at runtime from:

```text
https://raw.githubusercontent.com/samarthsubramanya/samarthagasthya.github.io/refs/heads/revamp_v4/src/data/portfolioData.json
```

Update that JSON file in the `revamp_v4` branch to change portfolio content in one place. No local `portfolioData.json` is bundled into this site.
