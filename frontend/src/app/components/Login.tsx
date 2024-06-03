"use client";

import React, { useContext, useState } from "react";
import { useForm } from "react-hook-form";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { AuthContext } from "./AuthContext";

export interface FieldsLogin {
  username: string;
  password: string;
}

const LoginForm: React.FC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FieldsLogin>();

  const [error, setError] = useState<string | null>(null);
  const { signIn } = useContext(AuthContext);
  const router = useRouter();

  const onSubmitHandler = async (data: FieldsLogin) => {
    try {
      await signIn(data);
      router.push("/dashboard");
    } catch (error) {
      setError("Error");
    }
  };

  return (
    <div className="max-w-sm mx-auto mt-8">
      <h1 className="text-3xl font-bold text-center mb-8">LOGIN</h1>
      <form onSubmit={handleSubmit(onSubmitHandler)}>
        <div className="mb-4">
          <label htmlFor="username" className="block text-white">
            Username
          </label>
          <input
            type="text"
            id="username"
            style={{ color: "black" }}
            {...register("username", { required: "Username is required" })}
            className={`mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50 ${
              errors.username ? "border-red-500" : ""
            }`}
          />
          {errors.username && (
            <p className="text-sm text-red-500">{errors.username.message}</p>
          )}
        </div>
        <div className="mb-4">
          <label htmlFor="password" className="block text-white">
            Password
          </label>
          <input
            type="password"
            id="password"
            style={{ color: "black" }}
            {...register("password", { required: "Password is required" })}
            className={`mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50 ${
              errors.password ? "border-red-500" : ""
            }`}
          />
          {errors.password && (
            <p className="text-sm text-red-500">{errors.password.message}</p>
          )}
        </div>
        <button
          type="submit"
          className="w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
        >
          Login
        </button>
        {error && (
          <div className="mt-4 p-2 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}
      </form>
      <label className="text-white block text-center">
        Not registered yet?{" "}
        <Link href="/register" className="text-blue-500">
          Register
        </Link>
      </label>
    </div>
  );
};

export default LoginForm;
