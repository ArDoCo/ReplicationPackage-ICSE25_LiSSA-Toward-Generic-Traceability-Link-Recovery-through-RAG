# Set output width for better visibility
options(width = 10000)

# Load necessary libraries
library(rstatix)  # For Wilcoxon test and effect size calculations
library(dplyr)    # For data manipulation

# Load datasets
r2c_baseline <- read.csv("Req2Code_Baselines.csv")
r2c_lissa <- read.csv("Req2Code_LiSSA_None-None-CoT.csv")

# Log dataset information
cat("Datasets successfully loaded.\n")
cat("Baseline dataset: Rows:",
    nrow(r2c_baseline),
    "Columns:",
    ncol(r2c_baseline),
    "\n")
cat("LiSSA dataset: Rows:",
    nrow(r2c_lissa),
    "Columns:",
    ncol(r2c_lissa),
    "\n")

# Extract unique baseline approaches
baseline_approaches <- unique(r2c_baseline$Approach)
metric_cols <- setdiff(colnames(r2c_baseline), "Approach")  # Remove "Approach" column

# Initialize result dataframes with NA values (except for the "Approach" column)
wilcox_p_values <- r2c_baseline
wilcox_p_values[, -1] <- NA

wilcox_eff_size_values <- r2c_baseline
wilcox_eff_size_values[, -1] <- NA

cat("Initialized result storage for Wilcoxon test and effect size calculations.\n")

# Loop over each baseline approach
for (baseline_name in baseline_approaches) {
  cat("Processing baseline approach:", baseline_name, "\n")

  # Loop through each metric column
  for (metric in metric_cols) {
    cat(" - Analyzing metric:", metric, "\n")

    # Extract values for the LiSSA approach
    approach_values <- r2c_lissa[[metric]]

    # Extract the corresponding baseline value
    baseline_value <- r2c_baseline[[metric]][r2c_baseline$Approach == baseline_name]

    # Check if baseline_value is NOT NA and has at least one value
    if (!is.na(baseline_value) && length(baseline_value) > 0) {
      cat("   Performing Wilcoxon signed-rank test...\n")

      # Perform Wilcoxon signed-rank test
      wilcox_test_result <- wilcox_test(data.frame(value = approach_values), value ~ 1, mu = baseline_value)

      # Compute effect size
      wilcox_eff_size_result <- wilcox_effsize(data.frame(value = approach_values),
                                               value ~ 1,
                                               mu = baseline_value,
                                               ci = TRUE)

      # Store p-value
      wilcox_p_values[wilcox_p_values$Approach == baseline_name, metric] <- wilcox_test_result$p

      # Store effect size in formatted form
      wilcox_eff_size_values[wilcox_p_values$Approach == baseline_name, metric] <- sprintf(
        "%.3f (%s) [%.2f, %.2f]",
        wilcox_eff_size_result$effsize,
        wilcox_eff_size_result$magnitude,
        wilcox_eff_size_result$conf.low,
        wilcox_eff_size_result$conf.high
      )

      cat("   Wilcoxon test completed. p-value:",
          wilcox_test_result$p,
          "\n")
    } else {
      cat("   Skipping metric due to missing values.\n")
    }
  }
}

# Calculate Mean and Standard Deviation **AFTER** the loop
cat("\nCalculating mean and standard deviation for LiSSA dataset...\n")

mean_standard_derivation_values <- as.data.frame(t(sapply(r2c_lissa[, metric_cols], function(x) {
  sprintf("%.3f ± %.3f [%d]",
          mean(x, na.rm = TRUE),
          sd(x, na.rm = TRUE),
          length(na.omit(x)))
})))

# Add column names for clarity
colnames(mean_standard_derivation_values) <- metric_cols

# Save Mean and Standard Deviation as **ONE ROW ONLY** (No "Approach" column)
cat("Mean and standard deviation calculated successfully.\n")

# Print results for verification
cat("\nWilcoxon p-values:\n")
print(wilcox_p_values)

cat("\nWilcoxon effect sizes:\n")
print(wilcox_eff_size_values)

cat("\nMean and standard deviation values:\n")
print(mean_standard_derivation_values)

# Save results to CSV files
write.csv(wilcox_p_values, "r2c_wilcox_p_values.csv", row.names = FALSE)
write.csv(wilcox_eff_size_values,
          "r2c_wilcox_eff_size_values.csv",
          row.names = FALSE)
write.csv(
  mean_standard_derivation_values,
  "r2c_mean_standard_derivation_values.csv",
  row.names = FALSE
)

cat("\nAll results have been saved to CSV files successfully.\n")
