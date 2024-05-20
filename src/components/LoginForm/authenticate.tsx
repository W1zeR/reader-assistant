import { signIn } from "next-auth/react";
import AuthError from "next-auth";
import { browserName, isMobile } from "react-device-detect";
import { authOptions } from "@/utils/authOptions";

export default async function authenticate(prevState: string | undefined, formData: FormData) {
  try {
    await signIn(
      "credentials", {
        email: formData.get("email"),
        password: formData.get("password"),
        browserName: browserName,
        deviceType: isMobile ? "mobile" : "desktop"
      });
  } catch (error) {
    if (error instanceof AuthError(authOptions)) {
      switch (error.type) {
        case "CredentialsSignin":
          return "Invalid credentials.";
        default:
          return "Something went wrong.";
      }
    }
    throw error;
  }
}