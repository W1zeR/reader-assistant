import { DefaultSession } from "next-auth";

declare module "next-auth" {
  interface Session extends DefaultSession {
    userId: number;
    accessToken: string;
    accessTokenExpiry: number;
    error: string | undefined;
  }

  interface User {
    userId: number;
    accessToken: string;
    refreshToken: string;
    accessTokenExpiry: number;
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    userId: number | null;
    accessToken: string | null;
    refreshToken: string | null;
    accessTokenExpiry: number | null;
    error: string | null;
  }
}
