const { initializeTestEnvironment, assertFails, assertSucceeds } = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

const PROJECT_ID = "demo-no-project";
let testEnv;

before(async () => {
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules: fs.readFileSync("firestore.rules", "utf8"),
      host: "127.0.0.1",
      port: 8085
    }
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read transactions", async () => {
  const unauthedDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthedDb.collection("users").doc("user_a").collection("transactions").get());
});

test("Authenticated user can read their own transactions", async () => {
  const authedDb = testEnv.authenticatedContext("user_a").firestore();
  await assertSucceeds(authedDb.collection("users").doc("user_a").collection("transactions").get());
});

test("User cannot read another user's transactions", async () => {
  const authedDb = testEnv.authenticatedContext("user_b").firestore();
  await assertFails(authedDb.collection("users").doc("user_a").collection("transactions").get());
});

test("Authenticated user can create valid transaction", async () => {
  const authedDb = testEnv.authenticatedContext("user_a").firestore();
  await assertSucceeds(
    authedDb.collection("users").doc("user_a").collection("transactions").add({
      userId: "user_a",
      date: "2026-10-25",
      account: "Bank",
      type: "out",
      amount: 1500.5,
      desc: "Bus Maintenance Service",
      category: "Maintenance"
    })
  );
});

test("Authenticated user cannot create invalid transaction with negative amount", async () => {
  const authedDb = testEnv.authenticatedContext("user_a").firestore();
  await assertFails(
    authedDb.collection("users").doc("user_a").collection("transactions").add({
      userId: "user_a",
      date: "2026-10-25",
      account: "Bank",
      type: "out",
      amount: -10,
      desc: "Bad amount"
    })
  );
});

test("User cannot write vehicle for another user", async () => {
  const authedDb = testEnv.authenticatedContext("user_b").firestore();
  await assertFails(
    authedDb.collection("users").doc("user_a").collection("vehicles").doc("v1").set({
      userId: "user_a",
      plate: "27804 (SHARJAH)"
    })
  );
});
