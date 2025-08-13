# HELLOJAVA
A simple hello world in java


# Java Spring Boot + New Relic (Codespaces) — Quick Start

This repo contains a minimal Spring Boot app with endpoints for demos:
- `/` — simple health text
- `/hello` — quick response
- `/slow` — returns after ~2 seconds (simulates latency)
- `/error-demo` — intentionally throws an exception (tests error tracking)

Use this guide to run it locally **in GitHub Codespaces** and attach the **New Relic Java agent** to capture APM data (transactions, traces, and errors).

---

## Prerequisites
- This project opened in **GitHub Codespaces**
- **Java 17+** (Codespaces default images include it)
- **Maven wrapper** included (`./mvnw`)
- A **New Relic** account + **license key**

> **Note:** Replace any example paths with your actual Codespaces path. On Codespaces it usually looks like `/workspaces/<your-repo-name>`.

---

## Run the app (without agent)
From the project root (the folder with `pom.xml` and `src/`):

```bash
./mvnw spring-boot:run
```

Open the forwarded **port 8080** and visit:
- `/` → `App is up. Try /hello, /slow, or /error-demo`
- `/hello`
- `/slow`
- `/error-demo` (will show a Whitelabel error page, that’s expected)

Stop the app with **Ctrl+C**.

---

## Add the New Relic Java agent (recommended setup)
This setup keeps secrets out of git and avoids nested folders.

### 1) Download the Java agent into a single folder
From the project root:
```bash
# download and unzip into a single 'newrelic' folder
curl -L -o newrelic-java.zip https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip
mkdir -p newrelic
unzip -o newrelic-java.zip -d newrelic
```

You should now have:
```
newrelic/
├── newrelic.jar
└── newrelic.yml
```

> If you accidentally ended up with a nested folder (e.g., `newrelic/newrelic/newrelic.jar`), move the JAR up one level:
> ```bash
> mv newrelic/newrelic/newrelic.jar newrelic/
> ```

### 2) Keep secrets out of git
```bash
# ignore the agent files and your env file
printf "newrelic/\n.env\n" >> .gitignore
```

### 3) Store your license key safely
Create a `.env` file in the project root (not committed to git) and add:

```bash
NEW_RELIC_LICENSE_KEY=YOUR_REAL_KEY_HERE
NEW_RELIC_APP_NAME="hellojava (codespaces)"
NEW_RELIC_DISTRIBUTED_TRACING_ENABLED=true
```

Load it in each new terminal session:
```bash
source .env
```

### 4) Run the app **with** the agent
Make sure the agent path points to your **actual** repo path and casing (Codespaces is case-sensitive). Example:

```bash
AGENT="$(pwd)/newrelic/newrelic.jar"
JAVA_TOOL_OPTIONS="-javaagent:${AGENT}" ./mvnw clean spring-boot:run
```

You should see logs like:
```
New Relic Agent: Initializing...
New Relic Agent: Loaded com.newrelic.bootstrap.BootstrapAgent
```
Open port 8080 and hit the endpoints to generate data:
- `/hello` (baseline)
- `/slow` (hit a few times to get traces)
- `/error-demo` (intentional 500 for Errors)

Then check your app in **APM & Services** with the name you set via `NEW_RELIC_APP_NAME`.

---

## Troubleshooting tips
- **Whitelabel Error Page on `/error-demo`**: expected — that’s a deliberate 500.
- **“Error opening zip file or JAR manifest missing”**: the agent path is wrong, the jar didn’t download, or you pointed to the `.zip`. Verify:
  ```bash
  ls -lh newrelic
  file newrelic/newrelic.jar        # should say 'Zip archive data'
  ```
- **No data in New Relic**: make sure you exported env vars in the same shell before running and that `NEW_RELIC_LICENSE_KEY` is correct.
- **Path casing**: `/workspaces/HELLOJAVA` ≠ `/workspaces/hellojava`. Match your folder name exactly.

---

## Handy NRQL queries
Use these in **Query your data** (or the Query Builder) once data appears:

**Recent transactions by name**
```sql
SELECT count(*) FROM Transaction
SINCE 30 minutes AGO
FACET name
LIMIT 20
```

**Slowest transactions**
```sql
SELECT average(duration) FROM Transaction
SINCE 30 minutes AGO
FACET name
LIMIT 10
```

**Recent errors**
```sql
SELECT count(*) FROM TransactionError
SINCE 30 minutes AGO
FACET error.message
LIMIT 10
```