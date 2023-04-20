package play

import future.keywords.contains
import future.keywords.if
import future.keywords.in

import data.policies.user as userConfig

user_is_admin if "admin" in userConfig.user_roles[input.user]
user_is_employee if "employee" in userConfig.user_roles[input.user]
user_is_customer if "customer" in userConfig.user_roles[input.user]
user_is_billing if "billing" in userConfig.user_roles[input.user]

