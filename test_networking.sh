#!/bin/bash

# This script executes the comprehensive test plan for the networking feature.
# Before running, make sure your Spring Boot application is running and that
# you have started with a fresh, empty database.

BASE_URL="http://localhost:8080/api/v1"
USER_ID=3

# Helper function to print section headers
print_header() {
    echo ""
    echo "===================================================="
    echo "$1"
    echo "===================================================="
    echo ""
}

# --- Part 1: Target Company Management ---
print_header "Part 1: Testing Target Company Management"

# Step 1: Register User
echo "--> Step 1.1: Registering a new user..."
curl -X POST "$BASE_URL/auth/register" \
-H "Content-Type: application/json" \
-d '{
    "username": "xavier_test2",
    "email": "xavier@example.com",
    "password": "a-strong-password"
}'
echo -e "\n"

# Step 2: Add Initial Targets
echo "--> Step 1.2: Adding 'Google' and 'Microsoft' as target companies..."
curl -X POST "$BASE_URL/users/$USER_ID/target-companies" -H "Content-Type: application/json" -d '{"companyName": "Google"}'
echo ""
curl -X POST "$BASE_URL/users/$USER_ID/target-companies" -H "Content-Type: application/json" -d '{"companyName": "Microsoft"}'
echo -e "\n"

echo "--> Verifying targets were added..."
curl "$BASE_URL/users/$USER_ID/target-companies"
echo -e "\n"

# Step 3: Edge Case - Add Duplicate Company
echo "--> Step 1.3: Testing edge case - adding a duplicate company (should fail)..."
curl -X POST "$BASE_URL/users/$USER_ID/target-companies" -H "Content-Type: application/json" -d '{"companyName": "Google"}'
echo -e "\n"

# Step 4: Delete a Target
echo "--> Step 1.4: Deleting a target company (Microsoft)..."
# NOTE: You may need to manually get the companyId for Microsoft from the verification step above.
# I will assume the ID is 2 for this script.
MICROSOFT_ID=2
curl -X DELETE "$BASE_URL/users/$USER_ID/target-companies/$MICROSOFT_ID"
echo -e "\n"

echo "--> Verifying target was deleted..."
curl "$BASE_URL/users/$USER_ID/target-companies"
echo -e "\n"


# --- Part 2: Automatic Status Updates ---
print_header "Part 2: Testing Automatic Status Updates"

# Step 1: Log a Coffee Chat
echo "--> Step 2.1: Logging a coffee chat for Google..."
curl -X POST "$BASE_URL/users/$USER_ID/networking-events" \
-H "Content-Type: application/json" \
-d '{
    "eventType": "COFFEE_CHAT",
    "contactName": "Jane Doe",
    "company": "Google",
    "eventDate": "2025-07-12",
    "notes": "Great chat!"
}'
echo -e "\n"

echo "--> Verifying status updated to COFFEE_CHAT_COMPLETED..."
curl "$BASE_URL/users/$USER_ID/target-companies"
echo -e "\n"

# Step 2: Log a Referral
echo "--> Step 2.2: Logging a referral for Google..."
curl -X POST "$BASE_URL/users/$USER_ID/networking-events" \
-H "Content-Type: application/json" \
-d '{
    "eventType": "REFERRAL_SECURED",
    "contactName": "Jane Doe",
    "company": "Google",
    "eventDate": "2025-07-12",
    "notes": "Secured referral."
}'
echo -e "\n"

echo "--> Verifying status updated to REFERRAL_SECURED..."
curl "$BASE_URL/users/$USER_ID/target-companies"
echo -e "\n"

# --- Part 3: Intelligent Quest Generation ---
print_header "Part 3: Testing Intelligent Quest Generation"

# Step 1: Setup the Scenario
echo "--> Step 3.1: Adding 'Netflix' as a new target..."
curl -X POST "$BASE_URL/users/$USER_ID/target-companies" -H "Content-Type: application/json" -d '{"companyName": "Netflix"}'
echo -e "\n"

# Step 2: Trigger Quest Generation
echo "--> Step 3.2: Triggering quest generation..."
curl -X POST "$BASE_URL/test/generate-quests"
echo -e "\n"

echo "--> Verifying new quest is for Netflix..."
curl "$BASE_URL/users/$USER_ID"
echo -e "\n"

echo "--> Test Plan Complete <--"