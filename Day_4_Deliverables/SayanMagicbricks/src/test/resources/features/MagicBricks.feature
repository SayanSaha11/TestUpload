# Feature: MagicBricks Buy Module – Automated Test Cases
# Covers: TC04, TC06, TC07, TC10, TC12, TC14, TC16

Feature: MagicBricks Buy Module Functional Validation

  Background:
    Given the user is on the MagicBricks Mumbai residential page

  # ─────────────────────────────────────────────
  # TC-04: Budget Homes – Price ceiling + EMI Calculator
  # ─────────────────────────────────────────────
  @TC04
  Scenario: TC04 - Budget Homes listings display prices within ceiling and EMI Calculator computes correctly
    When the user clicks the Buy heading
    And the user clicks "Budget Homes"
    And the user switches to the new tab
    Then the first 5 property prices should be displayed
    When the user clicks on the first property listing
    And the user switches to the property detail tab
    And the user clicks the Apply Loan button
    And the user switches to the loan calculator tab
    And the user enters loan amount "30,00,000" and interest rate "7.5"
    And the user selects property not finalized radio button
    And the user clicks Calculate EMI
    Then the EMI details should be displayed

  # ─────────────────────────────────────────────
  # TC-06: Premium Homes – Luxury listings + Image gallery navigation
  # ─────────────────────────────────────────────
  @TC06
  Scenario: TC06 - Premium Homes listings are displayed and image gallery navigates correctly
    When the user clicks the Buy heading
    And the user clicks "Premium Homes"
    And the user switches to the new tab
    And the user clicks the search button
    And the user opens the image gallery of the first property
    And the user clicks the Project Photo tab
    Then the user should be able to navigate right through 3 gallery images

  # ─────────────────────────────────────────────
  # TC-07: New Projects – Mandatory fields visible + Enquiry form submission
  # ─────────────────────────────────────────────
  @TC07
  Scenario: TC07 - New Projects display mandatory fields and enquiry form submits successfully
    Given the user navigates directly to New Projects Mumbai page
    Then the first 3 new project details should be displayed
    When the user clicks on the first new project
    And the user switches to the project detail tab
    And the user clicks Contact Seller
    And the user fills the enquiry form with name "Test User" email "testuser@gmail.com" and mobile "9163359056"
    And the user clicks Get Contact Details
    And the user selects the first top match
    Then the contact button text should be displayed

  # ─────────────────────────────────────────────
  # TC-10: House for Sale – Independent dwellings + Map View with locality
  # ─────────────────────────────────────────────
  @TC10
  Scenario: TC10 - House for Sale listings shown and Map View renders property location correctly
    When the user clicks the Buy heading
    And the user clicks "House for sale in Mumbai"
    And the user switches to the new tab
    And the user clicks on the specific house property
    And the user switches to the house detail tab
    And the user scrolls down by 1500 pixels
    And the user clicks About Locality tab
    And the user scrolls up by 100 pixels
    And the user clicks Explore Locality
    And the user switches to the locality tab
    And the user clicks the map view button
    Then the map should be displayed with the property marked

  # ─────────────────────────────────────────────
  # TC-12: Plot in Mumbai – No BHK filter + Area-based budget filter
  # ─────────────────────────────────────────────
  @TC12
  Scenario: TC12 - Plot listings have no BHK filter and area budget filter works correctly
    Given the user navigates directly to Plot in Mumbai page
    Then the current filter spec should be displayed
    When the user clicks More Filters
    And the user selects minimum budget "500" from dropdown
    And the user selects maximum budget "2000" from dropdown
    And the user clicks Apply Filters
    Then the updated filter spec should be displayed

  # ─────────────────────────────────────────────
  # TC-14: Office Space – Commercial listings + Contact Agent
  # ─────────────────────────────────────────────
  @TC14
  Scenario: TC14 - Office Space listings are shown and Contact Agent button is clickable
    When the user clicks the Buy heading
    And the user clicks "Office Space in Mumbai"
    And the user switches to the new tab
    And the user clicks on the first office property
    And the user switches to the office detail tab
    And the user clicks Contact Agent
    Then the Contact Agent action should be completed

  # ─────────────────────────────────────────────
  # TC-16: Commercial Space – Sub-type filter + Sort by Price High to Low
  # ─────────────────────────────────────────────
  @TC16
  Scenario: TC16 - Commercial Space supports sub-type filter and sort by price high to low
    When the user clicks the Buy heading
    And the user clicks "Commercial Space in Mumbai"
    And the user switches to the new tab
    And the user clicks the Business Type filter
    And the user selects the first business sub-type option
    And the user clicks the sort dropdown
    And the user selects "Price - High to Low" sort option
    Then the prices of the first 5 commercial properties should be displayed
